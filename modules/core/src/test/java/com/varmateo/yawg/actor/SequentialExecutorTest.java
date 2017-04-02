/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.yawg.actor.SequentialExecutor;
import com.varmateo.yawg.actor.SequentialExecutorTestUtils.Pojo;


/**
 *
 */
public final class SequentialExecutorTest {


    private static final int MAX_THREADS = 4;


    private final ExecutorService _delegateExecutor =
            Executors.newFixedThreadPool(MAX_THREADS);
    private SequentialExecutor _sequentialExecutor;


    /**
     *
     */
    @Before
    public void setUp() {

        _sequentialExecutor = new SequentialExecutor(_delegateExecutor);
    }


    /**
     *
     */
    @After
    public void teardown() {
        // Nothing to do, for now...
    }


    /**
     *
     */
    @Test
    public void whenOneTaskSubmitted_thenTaskGetsExecuted()
            throws Exception {

        // GIVEN
        Semaphore semaphore = new Semaphore(1);
        Pojo pojo = new Pojo();
        Runnable task = () -> pojo.doStuff("Hello, world!", 0, semaphore);

        // WHEN
        semaphore.acquire();
        _sequentialExecutor.execute(task);

        // THEN
        boolean isCompleted = semaphore.tryAcquire(
                1, 1_000, TimeUnit.MILLISECONDS);
        assertThat(isCompleted).isTrue();
        assertThat(pojo.getResult(0)).isEqualTo("Hello, world!");
    }


    /**
     *
     */
    @Test
    public void whenTaskThrowsError_thenNextTaskStillGetsExecuted()
            throws Exception {

        Semaphore semaphore = new Semaphore(1);
        Pojo pojo = new Pojo();
        Runnable taskFail = () -> pojo.throwError(0);
        Runnable taskOk = () -> pojo.doStuff("Hello, world!", 0, semaphore);

        // WHEN
        semaphore.acquire();
        _sequentialExecutor.execute(taskFail);
        _sequentialExecutor.execute(taskOk);

        // THEN
        boolean isCompleted = semaphore.tryAcquire(
                1, 1_000, TimeUnit.MILLISECONDS);
        assertThat(isCompleted).isTrue();
        assertThat(pojo.getResult(0)).isEqualTo("Hello, world!");
    }


    /**
     *
     */
    @Test
    public void whenLongRunningTasksSubmitted_thenTasksExecuteSequentially()
            throws Exception {

        Semaphore semaphore = new Semaphore(3);
        Pojo pojo = new Pojo();

        // WHEN
        semaphore.acquire(3);
        _sequentialExecutor.execute(
                () -> pojo.doStuff("result1", 300, semaphore));
        _sequentialExecutor.execute(
                () -> pojo.doStuff("result2", 200, semaphore));
        _sequentialExecutor.execute(
                () -> pojo.doStuff("result3", 100, semaphore));

        // THEN
        boolean isCompleted = semaphore.tryAcquire(
                3, 1_000, TimeUnit.MILLISECONDS);
        assertThat(isCompleted).isTrue();
        assertThat(pojo.getResults()).containsExactly(
                "result1",
                "result2",
                "result3");
    }


    /**
     *
     */
    @Test
    public void whenManyTasksSubmitted_thenTasksExecuteSequentially()
            throws Exception {

        int taskCount = 1_000;
        Semaphore semaphore = new Semaphore(taskCount);
        Pojo pojo = new Pojo();

        // WHEN
        semaphore.acquire(taskCount);
        for ( int i=0; i<taskCount; ++i ) {
            String value = "result" + i;
            _sequentialExecutor.execute(() -> pojo.doStuff(value, 0, semaphore));
        }

        // THEN
        boolean isCompleted = semaphore.tryAcquire(
                taskCount, 1_000, TimeUnit.MILLISECONDS);
        for ( int i=0; i<taskCount; ++i ) {
            assertThat(pojo.getResult(i)).isEqualTo("result" + i);
        }
    }


    /**
     *
     */
    @Test
    public void whenManyTasksSubmittedInParallel_thenTasksExecuteSequentially()
            throws Exception {

        int taskCount = 1_000;
        Semaphore semaphore = new Semaphore(taskCount);
        Pojo pojo = new Pojo();

        // WHEN
        semaphore.acquire(taskCount);
        for ( int i=0; i<taskCount; ++i ) {
            String value = "result" + i;
            Runnable task = () -> {
                _sequentialExecutor.execute(
                        () -> pojo.doStuff(value, 0, semaphore));
            };
            _delegateExecutor.execute(task);
        }

        // THEN
        boolean isCompleted = semaphore.tryAcquire(
                taskCount, 1_000, TimeUnit.MILLISECONDS);
        for ( int i=0; i<taskCount; ++i ) {
            assertThat(pojo.getResults()).contains("result" + i);
        }
    }


    /**
     *
     */
    @Test
    public void givenTwoSeqExecutors_whenTasksSubmittedToBoth_thenTasksExecuteInParallel()
            throws Exception {

        // GIVEN
        Semaphore semaphore = new Semaphore(4);
        Executor executor1 = new SequentialExecutor(_delegateExecutor);
        Pojo pojo1 = new Pojo();
        Executor executor2 = new SequentialExecutor(_delegateExecutor);
        Pojo pojo2 = new Pojo();

        // WHEN
        semaphore.acquire(4);
        executor1.execute(() -> pojo1.doStuff("result11", 300, semaphore));
        executor1.execute(() -> pojo1.doStuff("result12", 200, semaphore));
        executor2.execute(() -> pojo2.doStuff("result21", 300, semaphore));
        executor2.execute(() -> pojo2.doStuff("result22", 200, semaphore));

        // THEN
        boolean isCompleted = semaphore.tryAcquire(
                4, 750, TimeUnit.MILLISECONDS);
        assertThat(pojo1.getResults()).containsExactly(
                "result11",
                "result12");
        assertThat(pojo2.getResults()).containsExactly(
                "result21",
                "result22");
    }


    /**
     *
     */
    @Test
    public void givenNSeqExecutors_whenTasksSubmittedToAll_thenTasksExecuteInParallel()
            throws Exception {

        int actionDuration = 200;
        int parLevel = Runtime.getRuntime().availableProcessors();
        int pojoCount = parLevel;
        int taskCountPerPojo = 1;

        SequentialExecutorTestUtils.testParallelExecutionDuration(
                actionDuration, parLevel, pojoCount, taskCountPerPojo);
    }


}
