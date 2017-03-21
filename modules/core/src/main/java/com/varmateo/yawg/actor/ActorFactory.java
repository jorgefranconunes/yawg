/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import com.varmateo.yawg.actor.ActorRef;


/**
 *
 */
public interface ActorFactory<T> {


    /**
     *
     */
    T newActorCore(ActorRef actorSelf);

}
