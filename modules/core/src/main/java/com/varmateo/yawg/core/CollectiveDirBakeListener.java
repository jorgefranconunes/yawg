/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import io.vavr.collection.Seq;

import com.varmateo.yawg.api.PageVars;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.PageContext;


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
        PageContext augmentedContext = PageContext.builder(context)
                .setPageVars(newVars)
                .build();

        return augmentedContext;
    }


}
