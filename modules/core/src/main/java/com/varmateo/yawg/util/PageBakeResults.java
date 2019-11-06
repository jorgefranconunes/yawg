/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import io.vavr.control.Try;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.PageBakeResult;


/**
 *
 */
public final class PageBakeResults {


    private PageBakeResults() {
        // Nothing to do.
    }


    /**
     *
     */
    public static PageBakeResult success() {

        return new PageBakeResult() {

            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public YawgException failureCause() {
                throw new UnsupportedOperationException();
            }
        };
    }


    /**
     *
     */
    public static PageBakeResult failure(final YawgException cause) {

        return new PageBakeResult() {

            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public YawgException failureCause() {
                return cause;
            }
        };
    }


    /**
     *
     */
    public static PageBakeResult fromTry(final Try<Void> tryResult) {

        return tryResult
                .map(x -> PageBakeResults.success())
                .recover(YawgException.class, PageBakeResults::failure)
                .get();
    }


    /**
     *
     */
    public static Try<Void> toTry(final PageBakeResult result) {

        if ( result.isSuccess() ) {
            return Trys.success();
        } else {
            return Try.failure(result.failureCause());
        }
    }
}
