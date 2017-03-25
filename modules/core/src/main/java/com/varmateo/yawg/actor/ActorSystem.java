/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.Executor;

import com.varmateo.yawg.actor.ActorFactory;


/**
 *
 */
public final class ActorSystem {


    private final Executor _executor;


    /**
     *
     */
    public ActorSystem(final Executor executor) {

        _executor = executor;
    }


    /**
     *
     */
    public <T> T createActor(
            final Class<T> actorType,
            final ActorFactory<T> actorFactory) {

        ActorRef<T> actorRef = new ActorRef<>(actorType, _executor);
        T actorCore = actorFactory.newActorCore(actorRef);

        actorRef.setActorCore(actorCore);

        return actorRef.self();
    }

}
