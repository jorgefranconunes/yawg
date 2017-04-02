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
final class RefereeActor
        implements Referee {


    private final Semaphore _endGameSemaphore;
    private final String _gameName;
    private final boolean _isVerbose;

    private long _startTimeNanos = -1;
    private long _gameDurationNanos = -1;
    private int _playCount = -1;
    private boolean _isGameEnded = false;


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    private RefereeActor(final Builder builder) {

        _endGameSemaphore = Objects.requireNonNull(builder._endGameSemaphore);
        _gameName = Objects.requireNonNull(builder._gameName);
        _isVerbose = builder._isVerbose;
    }


    /**
     *
     */
    @Override
    public void startGame(
            final Player player1,
            final Player player2) {

        _startTimeNanos = System.nanoTime();
        log(() -> System.out.format("Starting %1$s\n", _gameName));
        player1.makeOnePlay(1, player2);
    }


    /**
     *
     */
    @Override
    public void endGame(final int playCount) {

        _gameDurationNanos = System.nanoTime() - _startTimeNanos;
        _playCount = playCount;
        _isGameEnded = true;

        log(() -> System.out.format(
                "%1$s ended after %2$d plays (%3$s ms)\n",
                _gameName,
                playCount,
                Fmt.nanosAsMillis(_gameDurationNanos)));
        _endGameSemaphore.release();
    }


    /**
     *
     */
    @Override
    public long getGameDurationNanos() {

        checkGameIsEnded();

        return _gameDurationNanos;
    }


    /**
     *
     */
    @Override
    public int getPlayCount() {

        checkGameIsEnded();

        return _playCount;
    }


    /**
     *
     */
    private void checkGameIsEnded() {

        if ( !_isGameEnded ) {
            String msg = "Game \"" + _gameName + "\" has not yet ended";
            throw new IllegalStateException(msg);
        }
    }


    /**
     *
     */
    private void log(final Runnable action) {

        if ( _isVerbose ) {
            action.run();
        }
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
        public Referee build() {

            return Objects.requireNonNull(_actorSystem).createActor(
                    actorRef -> new RefereeActor(this),
                    Referee.class);
        }

    }


}
