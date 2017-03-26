/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;


/**
 * A wrapper for an actor instance.
 *
 * <p>Instances of this class are tipically used only inside the class
 * implementing an actor. The <code>ActorRef</code> allows the actor
 * to execute lambdas and callbacks without closing on its internal
 * context.</p>
 *
 * <p>When an actor is created an <code>ActorRef</code> instance is
 * passed to its factory (see
 * <code>{@link ActorSystem#createActor(Class,ActorFactory)}</code>. The
 * actor can then use that <code>ActorRef</code> to execute lambdas,
 * by calling <code>{@link #perform(Runnable)}</code>, or to pass references
 * to itself to outside, by calling <code>{@link #self()}</code>.</p>
 */
public interface ActorRef<T> {


    /**
     * Performs an action in the scope of this actor. The given action
     * will be executed as if it had been called as a method of this
     * actor. This means that when the given action is being executed
     * no other thread will be executing methods of the actor.
     */
    void perform(final Runnable action);


    /**
     * Fetches the proxy actor for this <code>ActorRef</code>.
     *
     * @return The associated proxy actor.
     */
    T self();


}
