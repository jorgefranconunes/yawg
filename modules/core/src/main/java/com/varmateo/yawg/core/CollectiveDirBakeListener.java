/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Collection;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.PageVars;

import com.varmateo.yawg.util.Lists;


/**
 *
 */
/* package private */ final class CollectiveDirBakeListener
        extends Object
        implements DirBakeListener {


    private final Collection<DirBakeListener> _listeners;


    /**
     *
     */
    CollectiveDirBakeListener(final Collection<DirBakeListener> listeners) {

        _listeners = Lists.readOnlyCopy(listeners);
    }


    /**
     *
     */
    @Override
    public PageVars onDirBake(final PageContext context) {

        PageContext result = context;

        for ( DirBakeListener listener : _listeners ) {
            PageVars newVars = listener.onDirBake(result);
            result = buildContext(result, newVars);
        }

        return result.getPageVars();
    }


    /**
     *
     */
    private static PageContext buildContext(
            final PageContext oldData,
            final PageVars newVars) {

        PageContext result =
                PageContext.builder(oldData)
                .setPageVars(newVars)
                .build();

        return result;
    }


}
