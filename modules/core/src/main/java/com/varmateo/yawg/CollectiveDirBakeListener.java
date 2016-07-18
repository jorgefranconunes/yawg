/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Collection;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateVars;
import com.varmateo.yawg.util.Lists;


/**
 *
 */
final class CollectiveDirBakeListener
        extends Object
        implements DirBakeListener {


    private final Collection<DirBakeListener> _listeners;


    /**
     *
     */
    public CollectiveDirBakeListener(
            final Collection<DirBakeListener> listeners) {

        _listeners = Lists.readOnlyCopy(listeners);
    }


    /**
     *
     */
    @Override
    public TemplateVars onDirBake(final PageContext context) {

        PageContext result = context;

        for ( DirBakeListener listener : _listeners ) {
            TemplateVars newVars = listener.onDirBake(result);
            result = buildContext(result, newVars);
        }

        return result.templateVars;
    }


    /**
     *
     */
    private static PageContext buildContext(
            final PageContext oldData,
            final TemplateVars newVars) {

        PageContext result =
                PageContext.builder(oldData)
                .setTemplateVars(newVars)
                .build();

        return result;
    }


}
