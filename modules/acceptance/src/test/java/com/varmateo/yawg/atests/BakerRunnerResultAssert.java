/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.atests;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;

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
                    "Expected exit status to be zero but was %d",
                    actual.getExitStatus());
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
