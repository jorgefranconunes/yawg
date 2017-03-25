/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.yawg.actor.SequentialExecutor;
import com.varmateo.yawg.util.Lists;


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

        CompletableFuture<String> future = new CompletableFuture<>();
        Runnable task = () -> future.complete("Hello, world!");

        // WHEN
        _sequentialExecutor.execute(task);
        String actualResult = future.get(1000, TimeUnit.MILLISECONDS);

        // THEN
        assertThat(actualResult).isEqualTo("Hello, world!");
    }


    /**
     *
     */
    @Test
    public void whenTaskThrowsError_thenNextTaskStillGetsExecuted()
            throws Exception {

        CompletableFuture<String> future = new CompletableFuture<>();
        Runnable taskFail = () -> { throw new RuntimeException("boom"); };
        Runnable taskOk = () -> future.complete("Hello, world!");

        // WHEN
        _sequentialExecutor.execute(taskFail);
        _sequentialExecutor.execute(taskOk);
        String actualResult = future.get(1000, TimeUnit.MILLISECONDS);

        // THEN
        assertThat(actualResult).isEqualTo("Hello, world!");
    }


    /**
     *
     */
    @Test
    public void whenLongRunningTasksSubmitted_thenTasksExecuteSequentially()
            throws Exception {

        CompletableFuture<String> future1 = new CompletableFuture<>();
        CompletableFuture<String> future2 = new CompletableFuture<>();
        CompletableFuture<String> future3 = new CompletableFuture<>();
        Pojo pojo = new Pojo();

        // WHEN
        _sequentialExecutor.execute(
                () -> pojo.doStuff(300, future1, "result1"));
        _sequentialExecutor.execute(
                () -> pojo.doStuff(200, future2, "result2"));
        _sequentialExecutor.execute(
                () -> pojo.doStuff(100, future3, "result3"));

        // THEN
        assertThat(future1.get(1000, TimeUnit.MILLISECONDS))
                .isEqualTo("result1");
        assertThat(future2.get(1000, TimeUnit.MILLISECONDS))
                .isEqualTo("result2");
        assertThat(future3.get(1000, TimeUnit.MILLISECONDS))
                .isEqualTo("result3");
    }


    /**
     *
     */
    @Test
    public void whenManyTasksSubmitted_thenTasksExecuteSequentially()
            throws Exception {

        int taskCount = 10_000;
        @SuppressWarnings({"unchecked", "rawtypes"})
        CompletableFuture<String>[] futures =
                (CompletableFuture<String>[])new CompletableFuture[taskCount];
        Pojo pojo = new Pojo();

        // WHEN
        for ( int i=0; i<taskCount; ++i ) {
            String futureResult = "result" + i;
            CompletableFuture<String> future = new CompletableFuture<>();
            futures[i] = future;
            _sequentialExecutor.execute(
                    () -> pojo.doStuff(0, future, futureResult));
        }

        // THEN
        CompletableFuture.allOf(futures).get(1_000, TimeUnit.MILLISECONDS);
        for ( int i=0; i<taskCount; ++i ) {
            assertThat(futures[i]).isCompletedWithValue("result" + i);
        }
    }


    /**
     *
     */
    @Test
    public void whenManyTasksSubmittedInParallel_thenTasksExecuteSequentially()
            throws Exception {

        int taskCount = 10_000;
        @SuppressWarnings({"unchecked", "rawtypes"})
        CompletableFuture<String>[] futures =
                (CompletableFuture<String>[])new CompletableFuture[taskCount];
        Pojo pojo = new Pojo();

        // WHEN
        for ( int i=0; i<taskCount; ++i ) {
            String futureResult = "result" + i;
            CompletableFuture<String> future = new CompletableFuture<>();
            futures[i] = future;
            Runnable task = () -> {
                _sequentialExecutor.execute(
                        () -> pojo.doStuff(0, future, futureResult));
            };
            _delegateExecutor.execute(task);
        }

        // THEN
        CompletableFuture.allOf(futures).get(1000, TimeUnit.MILLISECONDS);
        for ( int i=0; i<taskCount; ++i ) {
            assertThat(futures[i]).isCompletedWithValue("result" + i);
        }
    }


    /**
     *
     */
    @Test
    public void givenTwoSeqExecutors_whenTasksSubmittedToBoth_thenTasksExecuteInParallel()
            throws Exception {
        // GIVEN
        Executor executor1 = new SequentialExecutor(_delegateExecutor);
        CompletableFuture<String> future11 = new CompletableFuture<>();
        CompletableFuture<String> future12 = new CompletableFuture<>();
        Pojo pojo1 = new Pojo();

        Executor executor2 = new SequentialExecutor(_delegateExecutor);
        CompletableFuture<String> future21 = new CompletableFuture<>();
        CompletableFuture<String> future22 = new CompletableFuture<>();
        Pojo pojo2 = new Pojo();

        long startTime = System.currentTimeMillis();

        // WHEN
        executor1.execute(() -> pojo1.doStuff(300, future11, "result11"));
        executor1.execute(() -> pojo1.doStuff(200, future12, "result12"));
        executor2.execute(() -> pojo2.doStuff(300, future21, "result21"));
        executor2.execute(() -> pojo2.doStuff(200, future22, "result22"));

        // THEN
        CompletableFuture.allOf(future11, future12, future21, future22)
                .get(750, TimeUnit.MILLISECONDS);
        assertThat(future11).isCompletedWithValue("result11");
        assertThat(future12).isCompletedWithValue("result12");
        assertThat(future21).isCompletedWithValue("result21");
        assertThat(future22).isCompletedWithValue("result22");
    }


    /**
     *
     */
    private static final class Pojo {


        private int _counter = 0;


        public void doStuff(
                final long delay,
                final CompletableFuture<String> future,
                final String value) {

            if ( _counter > 0 ) {
                throw new IllegalStateException("concurrent access");
            }

            ++ _counter;

            if ( delay > 0 ) {
                try {
                    Thread.sleep(delay);
                } catch ( InterruptedException e ) {
                    throw new RuntimeException(e);
                }
            }

            --_counter;
            future.complete(value);
        }

    }


}
