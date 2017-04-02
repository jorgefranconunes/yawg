/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor.pingpong;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import com.varmateo.yawg.actor.ActorSystem;
import com.varmateo.yawg.actor.ActorRef;


/**
 *
 */
final class PingPongGameSet {


    private final ActorSystem _actorSystem;
    private final boolean _isVerbose;


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    private PingPongGameSet(final Builder builder) {

        _actorSystem = Objects.requireNonNull(builder._actorSystem);
        _isVerbose = builder._isVerbose;
    }


    /**
     *
     */
    public void playGames(
            final int gameCount,
            final int maxPlayCount,
            final int maxWaitTimeMillis)
            throws Exception {

        long startTime = System.nanoTime();

        System.out.println(String.format(
                "Starting %1$d games of %2$d plays...",
                gameCount,
                maxPlayCount));

        List<PingPongGame> games =
                doPlayGames(gameCount, maxPlayCount, maxWaitTimeMillis);

        printStatistics(maxPlayCount, startTime, games);
    }


    /**
     *
     */
    private List<PingPongGame> doPlayGames(
            final int gameCount,
            final int maxPlayCount,
            final int maxWaitTimeMillis)
            throws Exception {

        // GIVEN
        Semaphore endGameSemaphore = new Semaphore(gameCount);
        List<PingPongGame> games =
                Stream.iterate(1, i -> i+1)
                .limit(gameCount)
                .map(i -> String.format("Game %1$d", i))
                .map(gameName -> buildGame(endGameSemaphore, gameName))
                .collect(Collectors.toList());

        // WHEN
        endGameSemaphore.acquire(gameCount);
        games.stream().forEach(game -> game.play(maxPlayCount));

        // THEN
        boolean isGameCompleted = endGameSemaphore.tryAcquire(
                gameCount, maxWaitTimeMillis, TimeUnit.MILLISECONDS);

        assertThat(isGameCompleted).isTrue();
        games.stream().forEach(game -> {
                assertThat(game.getPlayCount())
                        .as("%s play count", game.getGameName())
                        .isEqualTo(maxPlayCount);
            });


        return games;
    }


    /**
     *
     */
    private PingPongGame buildGame(
            final Semaphore endGameSemaphore,
            final String gameName) {

        return PingPongGame.builder()
                .setActorSystem(_actorSystem)
                .setEndGameSemaphore(endGameSemaphore)
                .setGameName(gameName)
                .setVerbose(_isVerbose)
                .build();
    }


    /**
     *
     */
    private void printStatistics(
            final int maxPlayCount,
            final long startTime,
            final List<PingPongGame> games) {

        int gameCount = games.size();
        long totalGameTime = System.nanoTime() - startTime;
        long sumOfPlayCounts = games.stream()
                .collect(Collectors.summingLong(g -> g.getPlayCount()));
        long sumOfGameTimes = games.stream()
                .collect(Collectors.summingLong(g -> g.getGameDurationNanos()));
        long averageGameTime = sumOfGameTimes / gameCount;
        long averagePlayTime = sumOfGameTimes / sumOfPlayCounts;

        if ( _isVerbose ) {
            System.out.println();
        }
        System.out.format(
                "Completed %1$d games of %2$d plays\n", gameCount, maxPlayCount);
        System.out.format(
                "Total game time   : %s ms\n", Fmt.nanosAsMillis(totalGameTime));
        System.out.format(
                "Average game time : %s ms\n", Fmt.nanosAsMillis(averageGameTime));
        System.out.format(
                "Average play time : %s ms\n", Fmt.nanosAsMillis(averagePlayTime));
        System.out.println();
    }


    /**
     *
     */
    public static final class Builder {


        private ActorSystem _actorSystem;
        private boolean _isVerbose;


        /**
         *
         */
        private Builder() {

            _actorSystem = null;
            _isVerbose = true;
        }


        /**
         *
         */
        public Builder setActorSystem(final ActorSystem actorSystem) {

            _actorSystem = actorSystem;
            return this;
        }


        /**
         *
         */
        public Builder setVerbose(final boolean isVerbose) {

            _isVerbose = isVerbose;
            return this;
        }


        /**
         *
         */
        public PingPongGameSet build() {

            return new PingPongGameSet(this);
        }

    }


}
