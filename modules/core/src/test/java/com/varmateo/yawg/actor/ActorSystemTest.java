/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.yawg.actor.ActorSystem;


/**
 *
 */
public final class ActorSystemTest {


    private static final int MAX_THREADS = 4;


    private final ExecutorService _executor =
            Executors.newFixedThreadPool(MAX_THREADS);

    private ActorSystem _actorSystem;


    /**
     *
     */
    @Before
    public void setUp() {

        _actorSystem = new ActorSystem(_executor);
    }


    /**
     *
     */
    @After
    public void tearDown() {
        // Nothing to do, yet...
    }


    /**
     *
     */
    @Test
    public void whenActorCreated_thenVoidMethodInvocationWorks()
            throws Exception {

        DummyActor actor = new DummyActor();
        Dummy dummy = _actorSystem.createActor(
                Dummy.class,
                actorRef -> actor);
        Semaphore semaphore = new Semaphore(1);

        // WHEN
        semaphore.acquire();
        dummy.saveValue(
                123,
                () -> semaphore.release());

        // THEN
        boolean isMethodCompleted =
                semaphore.tryAcquire(1_000, TimeUnit.MILLISECONDS);

        assertThat(isMethodCompleted).isTrue();
        assertThat(actor.getValue()).isEqualTo(123);
    }


    /**
     *
     */
    private static interface Dummy {

        void saveValue(
                int value,
                Runnable completionCallback);
    }


    /**
     *
     */
    private static final class DummyActor
            implements Dummy {


        private int _value = 0;


        /**
         *
         */
        @Override
        public void saveValue(
                final int value,
                final Runnable completionCallback) {

            _value = value;
            completionCallback.run();
        }


        /**
         *
         */
        public int getValue() {

            return _value;
        }


    }


}
