/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import com.varmateo.yawg.actor.SequentialExecutor;


/**
 * Utility functions used both in unit tests and integration tests.
 */
final class SequentialExecutorTestUtils {


    /**
     *
     */
    public static void testParallelExecutionDuration(
            final int actionDuration,
            final int parLevel,
            final int pojoCount,
            final int taskCountPerPojo)
            throws Exception {

        // GIVEN
        int taskCount = pojoCount * taskCountPerPojo;
        Semaphore semaphore = new Semaphore(taskCount);
        ExecutorService delegateExecutor =
                Executors.newFixedThreadPool(parLevel);
        List<Pojo> pojos = new ArrayList<>();
        List<Runnable> actions = new ArrayList<>();

        for ( int i=0; i<pojoCount; i++ ) {
            Pojo pojo = new Pojo();
            SequentialExecutor seqExecutor =
                    new SequentialExecutor(delegateExecutor);
            final int pojoIndex = i;
            Runnable action = () -> {
                for ( int j=0; j<taskCountPerPojo; j++ ) {
                    String value = "result-" + pojoIndex + "-" + j;
                    seqExecutor.execute(
                            () -> pojo.doStuff(value, actionDuration,semaphore));
                }
            };
            pojos.add(pojo);
            actions.add(action);
        }

        // WHEN
        semaphore.acquire(taskCount);
        for ( Runnable action : actions ) {
            action.run();
        }

        // THEN
        int maxExpectedPojoDuration = actionDuration*taskCountPerPojo;
        int maxExpectedDuration =
                ((pojoCount+parLevel-1)/parLevel) * maxExpectedPojoDuration
                + actionDuration/2;
        boolean isCompleted = semaphore.tryAcquire(
                taskCount, maxExpectedDuration, TimeUnit.MILLISECONDS);
        assertThat(isCompleted).isTrue();
        for ( int i=0; i<pojoCount; i++ ) {
            for ( int j=0; j<taskCountPerPojo; ++j ) {
                assertThat(pojos.get(i).getResult(j))
                        .isEqualTo("result-" + i + "-" + j);
            }
        }
    }


    /**
     *
     */
    public static final class Pojo {


        private int _counter = 0;
        private List<String> _results = new ArrayList<>();


        public void doStuff(
                final String value,
                final long delay,
                final Semaphore endActionSemaphore) {

            if ( _counter > 0 ) {
                throw new IllegalStateException("concurrent access");
            }

            ++ _counter;
            doSleep(delay);
            --_counter;
            _results.add(value);
            endActionSemaphore.release();
        }


        public void throwError(final long delay) {

            doSleep(delay);
            throw new RuntimeException("boom");
        }


        private void doSleep(final long delay) {

            if ( delay > 0 ) {
                try {
                    Thread.sleep(delay);
                } catch ( InterruptedException e ) {
                    throw new RuntimeException(e);
                }
            }
        }


        public String getResult(final int index) {

            if ( _results.size() == 0 ) {
                throw new IllegalStateException("Action has not been completed");
            }

            return _results.get(index);
        }


        public List<String> getResults() {

            if ( _results.size() == 0 ) {
                throw new IllegalStateException("Action has not been completed");
            }

            return _results;
        }

    }


}
