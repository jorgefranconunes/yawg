/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import org.junit.Ignore;
import org.junit.Test;

import com.varmateo.yawg.actor.SequentialExecutorTestUtils;


/**
 *
 */
public final class SequentialExecutorIT {


    /**
     *
     */
    @Ignore // Just for now...
    @Test
    public void givenNSeqExecutors_whenTasksSubmittedToAll_thenTasksExecuteInParallel()
            throws Exception {

        int actionDuration = 200;
        int cpuCount = Runtime.getRuntime().availableProcessors();

        SequentialExecutorTestUtils.testParallelExecutionDuration(
                actionDuration, 1*cpuCount, 1*cpuCount, 1);
        SequentialExecutorTestUtils.testParallelExecutionDuration(
                actionDuration, 1*cpuCount, 1*cpuCount, 100);

        SequentialExecutorTestUtils.testParallelExecutionDuration(
                actionDuration, 1*cpuCount, 10, 1);

        SequentialExecutorTestUtils.testParallelExecutionDuration(
                actionDuration, 1*cpuCount, 100, 1);

        SequentialExecutorTestUtils.testParallelExecutionDuration(
                actionDuration, 10*cpuCount, 20*cpuCount, 1);

        SequentialExecutorTestUtils.testParallelExecutionDuration(
                actionDuration, 10*cpuCount, 200*cpuCount, 1);
    }


}
