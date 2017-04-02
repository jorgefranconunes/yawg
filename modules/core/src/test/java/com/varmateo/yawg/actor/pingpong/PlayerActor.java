/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor.pingpong;

import java.util.Objects;

import com.varmateo.yawg.actor.ActorRef;
import com.varmateo.yawg.actor.ActorSystem;


/**
 *
 */
final class PlayerActor
        implements Player {


    private final Player _selfActor;
    private final String _name;
    private final String _sound;
    private final Referee _referee;
    private final int _maxPlayCount;
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
    private PlayerActor(
            final ActorRef<Player> actorRef,
            final Builder builder) {

        _selfActor = actorRef.self();
        _name = Objects.requireNonNull(builder._name);
        _sound = Objects.requireNonNull(builder._sound);
        _referee = Objects.requireNonNull(builder._referee);
        _maxPlayCount = Objects.requireNonNull(builder._maxPlayCount);
        _isVerbose = builder._isVerbose;
    }


    /**
     *
     */
    @Override
    public void makeOnePlay(
            final int playCount,
            final Player nextPlayer) {

        log(() -> System.out.format(
                "%1$s - %2$s - %3$d\n", _name, _sound, playCount));

        if ( playCount >= _maxPlayCount ) {
            _referee.endGame(playCount);
        } else {
            nextPlayer.makeOnePlay(playCount+1, _selfActor);
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
        private String _name;
        private String _sound;
        private Referee _referee;
        private Integer _maxPlayCount;
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
        public Builder setName(final String name) {

            _name = name;
            return this;
        }


        /**
         *
         */
        public Builder setSound(final String sound) {

            _sound = sound;
            return this;
        }


        /**
         *
         */
        public Builder setReferee(final Referee referee) {

            _referee = referee;
            return this;
        }


        /**
         *
         */
        public Builder setMaxPlayCount(final int maxPlayCount) {

            _maxPlayCount = maxPlayCount;
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
        public Player build() {

            return Objects.requireNonNull(_actorSystem).createActor(
                    actorRef -> new PlayerActor(actorRef, this),
                    Player.class);
        }
    }

}
