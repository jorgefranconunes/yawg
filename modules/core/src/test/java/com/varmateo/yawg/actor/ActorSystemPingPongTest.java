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
public final class ActorSystemPingPongTest {


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

        // GIVEN
        long startTime = System.currentTimeMillis();
        Semaphore endGameSemaphore = new Semaphore(gameCount);

        System.out.println(String.format(
                "Starting %1$d games of %2$d plays...",
                gameCount,
                maxPlayCount));

        // WHEN
        endGameSemaphore.acquire(gameCount);

        for ( int i=1; i<=gameCount; ++i ) {
            String gameName = String.format("Game %1$d", i);
            playOneGame(actorSystem, endGameSemaphore, gameName, maxPlayCount);
        }

        // THEN
        boolean isGameCompleted =
                endGameSemaphore.tryAcquire(
                        gameCount, maxWaitTimeMillis,
                        TimeUnit.MILLISECONDS);

        assertThat(isGameCompleted).isTrue();

        long delay = System.currentTimeMillis() - startTime;
        System.out.println(String.format(
                "Completed %1$d games of %2$d plays in %3$d ms",
                gameCount,
                maxPlayCount,
                delay));
    }


    /**
     *
     */
    private static void playOneGame(
            final ActorSystem actorSystem,
            final Semaphore endGameSemaphore,
            final String gameName,
            final int maxPlayCount) {

        Referee referee =
                RefereeActor.newRefereeActor(
                        actorSystem,
                        endGameSemaphore,
                        gameName);
        Player player1 =
                PlayerActor.newPlayerActor(
                        actorSystem,
                        gameName + " - Player 1",
                        "Ping",
                        referee,
                        maxPlayCount);
        Player player2 =
                PlayerActor.newPlayerActor(
                        actorSystem,
                        gameName + " - Player 2",
                        "Pong",
                        referee,
                        maxPlayCount);

        referee.startGame(player1, player2);
    }


    /**
     *
     */
    private interface Referee {

        void startGame(
                Player player1,
                Player player2);
        void endGame(int playCount);
    }


    /**
     *
     */
    private static final class RefereeActor
            implements Referee {


        private final Semaphore _endGameSemaphore;
        private final String _gameName;
        private long _startTime;


        /**
         *
         */
        public static Referee newRefereeActor(
                final ActorSystem actorSystem,
                final Semaphore endGameSemaphore,
                final String gameName) {

            return actorSystem.createActor(
                    Referee.class,
                    actorRef -> new RefereeActor(endGameSemaphore, gameName));
        }


        /**
         *
         */
        private RefereeActor(
                final Semaphore endGameSemaphore,
                final String gameName) {

            _endGameSemaphore = endGameSemaphore;
            _gameName = gameName;
        }


        /**
         *
         */
        @Override
        public void startGame(
                final Player player1,
                final Player player2) {

            _startTime = System.currentTimeMillis();
            System.out.println(String.format("Starting %1$s", _gameName));
            player1.makeOnePlay(1, player2);
        }


        /**
         *
         */
        @Override
        public void endGame(final int playCount) {

            long delay = System.currentTimeMillis() - _startTime;

            System.out.println(String.format(
                    "%1$s ended after %2$d plays (%3$d ms)",
                    _gameName,
                    playCount,
                    delay));
            _endGameSemaphore.release();
        }

    }


    /**
     *
     */
    private interface Player {

        void makeOnePlay(
                int playCount,
                Player nextPlayer);
    }


    /**
     *
     */
    private static final class PlayerActor
            implements Player {


        private final Player _selfActor;
        private final String _name;
        private final String _sound;
        private final Referee _referee;
        private final int _maxPlayCount;


        /**
         *
         */
        public static Player newPlayerActor(
                final ActorSystem actorSystem,
                final String name,
                final String sound,
                final Referee referee,
                final int maxPlayCount) {

            return actorSystem.createActor(
                    Player.class,
                    actorRef -> new PlayerActor(
                            actorRef,
                            name,
                            sound,
                            referee,
                            maxPlayCount));
        }


        /**
         *
         */
        private PlayerActor(
                final ActorRef<Player> actorRef,
                final String name,
                final String sound,
                final Referee referee,
                final int maxPlayCount) {

            _selfActor = actorRef.self();
            _name = name;
            _sound = sound;
            _referee = referee;
            _maxPlayCount = maxPlayCount;
        }


        /**
         *
         */
        @Override
        public void makeOnePlay(
                final int playCount,
                final Player nextPlayer) {

            System.out.println(String.format(
                    "%1$s - %2$s - %3$d", _name, _sound, playCount));

            if ( playCount >= _maxPlayCount ) {
                _referee.endGame(playCount);
            } else {
                nextPlayer.makeOnePlay(playCount+1, _selfActor);
            }
        }
    }


}
