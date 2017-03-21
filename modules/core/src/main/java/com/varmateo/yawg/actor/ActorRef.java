/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.Executor;

import com.varmateo.yawg.actor.SequentialExecutor;


/**
 *
 */
public final class ActorRef {


    private final Executor _sequentialExecutor;


    /**
     *
     */
    ActorRef(final Executor executor) {

        _sequentialExecutor = new SequentialExecutor(executor);
    }


    /**
     *
     */
    public void perform(final Runnable message) {

        _sequentialExecutor.execute(message);
    }


}
