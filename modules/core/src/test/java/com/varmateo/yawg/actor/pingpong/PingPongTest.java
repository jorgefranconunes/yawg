/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor.pingpong;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.yawg.actor.ActorSystem;


/**
 *
 */
public final class PingPongTest {


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
    public void givenOneGame_whenGameStarts_thenGameEndsAfterAgreedPlays()
            throws Exception {

        playManyGamesTest(_actorSystem, 1, 10, 1_000);
    }


    /**
     *
     */
    @Test
    public void givenTwoGames_whenGamesStart_thenGamesEndAfterAgreedPlays()
            throws Exception {

        playManyGamesTest(_actorSystem, 2, 10, 1_000);
    }


    /**
     *
     */
    private static void playManyGamesTest(
            final ActorSystem actorSystem,
            final int gameCount,
            final int maxPlayCount,
            final int maxWaitTimeMillis)
            throws Exception {

        PingPongGameSet game = PingPongGameSet.builder()
                .setActorSystem(actorSystem)
                .setVerbose(true)
                .build();

        game.playGames(gameCount, maxPlayCount, maxWaitTimeMillis);
    }


}
