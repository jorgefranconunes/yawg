/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import io.vavr.control.Try;

import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.api.YawgException;


/**
 *
 */
public final class Results {

    private Results() {
        // Nothing to do.
    }


    /**
     *
     */
    public static <T> Result<T> fromTry(final Try<T> tryResult) {

        return tryResult
                .map(Result::success)
                .recover(YawgException.class, Result::failure)
                .get();
    }


    /**
     *
     */
    public static <T> Try<T> toTry(final Result<T> result) {

        if (result.isSuccess()) {
            return Try.success(result.get());
        } else {
            return Try.failure(result.failureCause());
        }
    }
}
