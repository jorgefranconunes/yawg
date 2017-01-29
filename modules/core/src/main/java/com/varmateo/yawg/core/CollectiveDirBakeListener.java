/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import javaslang.collection.Seq;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.PageVars;


/**
 *
 */
/* package private */ final class CollectiveDirBakeListener
        implements DirBakeListener {


    private final Seq<DirBakeListener> _listeners;


    /**
     *
     */
    CollectiveDirBakeListener(final Seq<DirBakeListener> listeners) {

        _listeners = listeners;
    }


    /**
     *
     */
    @Override
    public PageVars onDirBake(final PageContext context) {

        return _listeners
                .foldLeft(
                        context,
                        (xs, listener) -> augmentContext(xs, listener))
                .getPageVars();
    }


    private static PageContext augmentContext(
            final PageContext context,
            final DirBakeListener listener) {

        PageVars newVars = listener.onDirBake(context);
        PageContext newContext = PageContext.builder(context)
                .setPageVars(newVars)
                .build();

        return newContext;
    }


}