/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor.pingpong;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.varmateo.yawg.actor.ActorSystem;
import com.varmateo.yawg.actor.ActorRef;


/**
 * Long running tests.
 */
public final class PingPongIT {


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
    public void givenManyGames_whenGamesStart_thenGamesEndAfterAgreedPlays()
            throws Exception {

        playManyGamesTest(_actorSystem, 1, 1_000_000, 30_000);
        playManyGamesTest(_actorSystem, 2, 1_000_000, 30_000);
        playManyGamesTest(_actorSystem, 100, 100_000, 30_000);
        playManyGamesTest(_actorSystem, 200, 100_000, 30_000);
        playManyGamesTest(_actorSystem, 10_000, 2, 30_000);
        playManyGamesTest(_actorSystem, 10_000, 100, 30_000);
        playManyGamesTest(_actorSystem, 10_000, 500, 30_000);
    }


    /**
     *
     */
    @Test
    @Ignore
    public void benchmark() {

        Stream.iterate(new FibonnaciSeq(), s -> s.next())
                .limit(16)
                .mapToLong(s -> s.value())
                .forEach(gameCount ->
                         Stream.iterate(new FibonnaciSeq(), s -> s.next())
                         .limit(16)
                         .mapToLong(s -> s.value())
                         .forEach(maxPlayCount ->
                                  playManyGamesTest(
                                          _actorSystem,
                                          (int)gameCount,
                                          (int)maxPlayCount,
                                          10_000)));
    }


    /**
     *
     */
    private static void playManyGamesTest(
            final ActorSystem actorSystem,
            final int gameCount,
            final int maxPlayCount,
            final int maxWaitTimeMillis) {

        PingPongGameSet game = PingPongGameSet.builder()
                .setActorSystem(actorSystem)
                .setVerbose(false)
                .build();

        try {
            game.playGames(gameCount, maxPlayCount, maxWaitTimeMillis);
        } catch ( Exception e ) {
            throw new RuntimeException(e);
        }
    }


    /**
     *
     */
    private static class FibonnaciSeq {


        private long _f_n = 1;
        private long _f_n_1 = 1;


        /**
         *
         */
        public FibonnaciSeq next() {

            long newValue = _f_n + _f_n_1;

            _f_n_1 = _f_n;
            _f_n = newValue;

            return this;
        }


        /**
         *
         */
        public long value() {

            return _f_n;
        }


    }


}
