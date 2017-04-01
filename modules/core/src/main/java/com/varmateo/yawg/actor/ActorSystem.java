/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.Executor;

import com.varmateo.yawg.actor.ActorFactory;
import com.varmateo.yawg.actor.ActorRefImpl;


/**
 * Manages a set of actors.
 *
 * <p>The actors managed by one given <code>ActorSystem</code> have
 * their methods executed by one of the threads in the
 * <code>Executor</code> specified at constructor time.</p>
 */
public final class ActorSystem {


    private final Executor _executor;


    /**
     * @param executor Used for executing actor methods.
     */
    public ActorSystem(final Executor executor) {

        _executor = executor;
    }


    /**
     * Creates a new actor.
     *
     * <p>A new actor instance is created through the provided
     * <code>{@link ActorFactory}</code>. A proxy for that actor
     * instance will then be returned. The actor proxy is the visible
     * face of the actor for the outside world. The actor proxy,
     * working within the actor system, has the responsability to
     * ensure the actor methods are called in sequence, and never
     * concurrently.</code>
     *
     * @param actorFactory Used for creating an actual actor
     * instance.
     *
     * @param actorType The class object of the interface the actor
     * instance implements. This will also be the type of the returned
     * proxy actor.
     *
     * @return An object implementing the given <code>actorType</code>
     * interface. That object is a proxy for the actor instance
     * created with the <code>ActorFactory</code>.
     */
    public <T> T createActor(
            final ActorFactory<T> actorFactory,
            final Class<T> actorType) {

        ActorRefImpl<T> actorRef = new ActorRefImpl<>(actorType, _executor);
        T actorCore = actorFactory.newActor(actorRef);

        actorRef.setActorCore(actorCore);

        return actorRef.self();
    }

}
