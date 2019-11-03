/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Writer;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;


/**
 *
 */
public final class MockTemplate
        implements Template {


    /**
     *
     */
    @Override
    public void process(
            TemplateContext model,
            Writer output)
            throws YawgException {

        try {
            output.write(model.body());
        } catch ( IOException e ) {
            throw MockTemplateException.failure(e);
        }
    }


    private static final class MockTemplateException
            extends YawgException {


        private MockTemplateException(
                final String msg,
                final Throwable cause) {

            super(msg, cause);
        }


        public static MockTemplateException failure(final IOException cause) {

            final String msg = String.format(
                    "Failed to write page - %s - %s",
                    cause.getClass().getName(),
                    cause.getMessage());

            return new MockTemplateException(msg, cause);
        }
    }


}


