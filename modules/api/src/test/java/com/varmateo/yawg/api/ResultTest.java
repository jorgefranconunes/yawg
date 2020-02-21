/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.util.NoSuchElementException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public final class ResultTest {


    @Test
    public void verifySuccess() {

        verifySuccess("hello");
        verifySuccess(42);
    }


    private <T> void verifySuccess(final T value) {

        final Result<T> actualResult = Result.success(value);
        final T expectedValue = value;

        assertThat(actualResult.isSuccess())
                .isTrue();

        assertThat(actualResult.get())
                .isEqualTo(expectedValue);

        assertThatThrownBy(() -> actualResult.failureCause())
                .isInstanceOf(NoSuchElementException.class);
    }


    @Test
    public void verifyFailure() {

        verifyFailure(new YawgException("boom") {});
    }


    private void verifyFailure(final YawgException cause) {

        final Result<?> actualResult = Result.failure(cause);
        final Throwable expectedCause = cause;

        assertThat(actualResult.isSuccess())
                .isFalse();

        assertThatThrownBy(() -> actualResult.get())
                .isInstanceOf(NoSuchElementException.class);

        assertThat(actualResult.failureCause())
                .isSameAs(expectedCause);
    }
}
