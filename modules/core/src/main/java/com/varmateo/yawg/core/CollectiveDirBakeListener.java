/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import io.vavr.collection.Seq;
import io.vavr.control.Try;

import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.OnDirBakeResult;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.util.OnDirBakeResults;


/**
 *
 */
/* default */ final class CollectiveDirBakeListener
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
    public OnDirBakeResult onDirBake(final PageContext context) {

        final Try<PageVars> tryResult = _listeners
                .foldLeft(
                        Try.success(context),
                        (acc, listener) -> augmentContext(acc, listener))
                .map(PageContext::pageVars);

        return OnDirBakeResults.fromTry(tryResult);
    }


    private static Try<PageContext> augmentContext(
            final Try<PageContext> tryContext,
            final DirBakeListener listener) {

        return tryContext
                .flatMap(
                        context -> OnDirBakeResults.toTry(listener.onDirBake(context))
                        .map(newVars -> buildPageContext(context, newVars)));
    }


    private static PageContext buildPageContext(
            final PageContext context,
            final PageVars newVars) {

        return PageContextBuilder.create(context)
                .pageVars(newVars)
                .build();
    }


}
