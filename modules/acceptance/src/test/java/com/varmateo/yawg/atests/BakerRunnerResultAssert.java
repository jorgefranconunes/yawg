/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;

import com.varmateo.yawg.atests.BakerRunnerResult;


/**
 *
 */
public final class BakerRunnerResultAssert
        extends AbstractAssert<BakerRunnerResultAssert, BakerRunnerResult> {


    /**
     *
     */
    public static BakerRunnerResultAssert assertThat(
            final BakerRunnerResult actual) {

        return new BakerRunnerResultAssert(actual);
    }


    /**
     *
     */
    public BakerRunnerResultAssert(final BakerRunnerResult actual) {

        super(actual, BakerRunnerResultAssert.class);
    }


    /**
     *
     */
    public BakerRunnerResultAssert hasExitStatusSuccess() {

        isNotNull();

        if ( actual.getExitStatus() != 0 ) {
            failWithMessage(
                    "Expected exit status to be zero but was %d - %s",
                    actual.getExitStatus(),
                    actual.getOutputAsString());
        }

        return this;
    }


    /**
     *
     */
    public BakerRunnerResultAssert hasExitStatusFailure() {

        isNotNull();

        if ( actual.getExitStatus() == 0 ) {
            failWithMessage("Expected exit status to be non-zero but was zero");
        }

        return this;
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputAsString() {

        isNotNull();

        return Assertions.assertThat(actual.outputAsString());
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputLine(final int index) {

        isNotNull();

        return Assertions.assertThat(actual.outputLine(index));
    }


    /**
     *
     */
    public AbstractCharSequenceAssert<?,String> outputLineFromEnd(
            final int index) {

        isNotNull();

        return Assertions.assertThat(actual.outputLineFromEnd(index));
    }


}
