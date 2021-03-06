/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.YawgException;


/**
 *
 */
/* default */ final class CollectiveTemplateServiceException
        extends YawgException {


    private CollectiveTemplateServiceException(final String msg) {
        super(msg);
    }


    /**
     *
     */
    public static CollectiveTemplateServiceException unsupportedTemplateFormat(final String name) {

        final String msg = String.format("Unsupported template format \"%s\"", name);

        return new CollectiveTemplateServiceException(msg);
    }
}
