/**************************************************************************
 *
 * Copyright (c) 2018 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.testutils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


/**
 *
 */
public final class LogStartAndEndRule implements TestRule
{

    private static final String MSG_START = ">>>>>> START - %s <<<<<<";
    private static final String MSG_END = ">>>>>> END - %s (%.3f ms) <<<<<<";

    /**
     *
     */
    @Override
    public Statement apply(
            final Statement base,
            final Description description)
    {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                runTest(base, description);
            }
        };
    }


    private void runTest(
            final Statement base,
            final Description description) throws Throwable
    {
        long startTime = System.nanoTime();
        String testName = description.getMethodName();
        Throwable error = null;

        String startMsg = String.format(MSG_START, testName);

        System.out.println(startMsg);

        try {
            base.evaluate();
        } catch ( Throwable e ) {
            error = e;
        }

        long durationNanos = System.nanoTime() - startTime;
        double durationMillis = durationNanos / 1_000_000.0;
        String endMsg = String.format(MSG_END, testName, durationMillis);

        System.out.println(endMsg);

        if ( error != null ) {
            throw error;
        }
    }

}
