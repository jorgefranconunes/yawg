/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import io.vavr.control.Try;

import com.varmateo.yawg.api.SiteBakeResult;
import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public final class SiteBakeResults {


    private SiteBakeResults() {
        // Nothing to do.
    }


    /**
     *
     */
    public static SiteBakeResult success() {

        return new SiteBakeResult() {

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
    public static SiteBakeResult failure(final YawgException cause) {

        return new SiteBakeResult() {

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
    public static SiteBakeResult fromTry(final Try<Void> tryResult) {

        return tryResult
                .map(x -> SiteBakeResults.success())
                .recover(YawgException.class, SiteBakeResults::failure)
                .get();
    }


    /**
     *
     */
    public static Try<Void> toTry(final SiteBakeResult result) {

        if ( result.isSuccess() ) {
            return Trys.success();
        } else {
            return Try.failure(result.failureCause());
        }
    }
}
