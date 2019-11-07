/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import io.vavr.control.Try;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.OnDirBakeResult;
import com.varmateo.yawg.spi.PageVars;


/**
 *
 */
public final class OnDirBakeResults {


    private OnDirBakeResults() {
        // Nothing to do.
    }


    /**
     *
     */
    public static OnDirBakeResult success(final PageVars pageVars) {

        return new OnDirBakeResult() {
            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public PageVars pageVars() {
                return pageVars;
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
    public static OnDirBakeResult failure(final YawgException cause) {

        return new OnDirBakeResult() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public PageVars pageVars() {
                throw new UnsupportedOperationException();
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
    public static OnDirBakeResult fromTry(final Try<PageVars> tryResult) {

        return tryResult
                .map(OnDirBakeResults::success)
                .recover(YawgException.class, OnDirBakeResults::failure)
                .get();
    }


    /**
     *
     */
    public static Try<PageVars> toTry(final OnDirBakeResult result) {

        if ( result.isSuccess() ) {
            return Try.success(result.pageVars());
        } else {
            return Try.failure(result.failureCause());
        }
    }
}
