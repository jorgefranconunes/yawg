/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Writer;

import io.vavr.control.Try;

import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.util.Results;


/**
 *
 */
public final class MockTemplate
        implements Template {


    /**
     *
     */
    @Override
    public Result<Void> process(
            TemplateContext model,
            Writer output) {

        final Try<Void> result = Try.run(() -> output.write(model.body()))
                .recoverWith(
                        IOException.class,
                        cause -> Try.failure(MockTemplateException.failure(cause)));

        return Results.fromTry(result);
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


