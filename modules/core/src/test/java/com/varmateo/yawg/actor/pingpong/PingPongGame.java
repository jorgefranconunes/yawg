/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor.pingpong;

import java.util.Objects;
import java.util.concurrent.Semaphore;

import com.varmateo.yawg.actor.ActorSystem;


/**
 *
 */
final class PingPongGame {


    private final ActorSystem _actorSystem;
    private final String _gameName;
    private final boolean _isVerbose;
    private final Referee _referee;


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    private PingPongGame(final Builder builder) {

        _actorSystem = Objects.requireNonNull(builder._actorSystem);
        _gameName = Objects.requireNonNull(builder._gameName);
        _isVerbose = builder._isVerbose;
        _referee = RefereeActor.builder()
                .setActorSystem(_actorSystem)
                .setEndGameSemaphore(builder._endGameSemaphore)
                .setGameName(_gameName)
                .setVerbose(_isVerbose)
                .build();
    }


    /**
     *
     */
    public void play(final int maxPlayCount) {

        Player player1 = PlayerActor.builder()
                .setActorSystem(_actorSystem)
                .setName(_gameName + " - Player 1")
                .setSound("Ping")
                .setReferee(_referee)
                .setMaxPlayCount(maxPlayCount)
                .setVerbose(_isVerbose)
                .build();
        Player player2 = PlayerActor.builder()
                .setActorSystem(_actorSystem)
                .setName(_gameName + " - Player 2")
                .setSound("Pong")
                .setReferee(_referee)
                .setMaxPlayCount(maxPlayCount)
                .setVerbose(_isVerbose)
                .build();

        _referee.startGame(player1, player2);
    }


    /**
     *
     */
    public long getGameDurationNanos() {

        return _referee.getGameDurationNanos();
    }


    /**
     *
     */
    public int getPlayCount() {

        return _referee.getPlayCount();
    }


    /**
     *
     */
    public String getGameName() {

        return _gameName;
    }


    /**
     *
     */
    public static final class Builder {


        private ActorSystem _actorSystem;
        private Semaphore _endGameSemaphore;
        private String _gameName;
        private boolean _isVerbose;


        /**
         *
         */
        private Builder() {

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
        public Builder setEndGameSemaphore(final Semaphore endGameSemaphore) {

            _endGameSemaphore = endGameSemaphore;
            return this;
        }


        /**
         *
         */
        public Builder setGameName(final String gameName) {

            _gameName = gameName;
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
        public PingPongGame build() {

            return new PingPongGame(this);
        }


    }


}
