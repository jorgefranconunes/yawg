/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import com.varmateo.yawg.actor.ActorRef;


/**
 * Contract for a factory of actors.
 *
 * <p>An <code>ActorFactory</code> is required for creating an actor
 * instance in the actor system through <code>{@link
 * ActorSystem#createActor(Class,ActorFactory)}</code>.
 */
@FunctionalInterface
public interface ActorFactory<T> {


    /**
     * Creates a new actor instance.
     *
     * <p>The factory method will receive the <code>{@link
     * ActorRef}</code> with which the new actor instance will be
     * associated to.</code>
     *
     * @param actorSelf The actor reference that the returned actor
     * instance will be associated to.
     *
     * @return A new actor instance.
     */
    T newActor(ActorRef<T> actorSelf);

}
