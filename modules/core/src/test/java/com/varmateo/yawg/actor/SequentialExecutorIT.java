/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.yawg.actor.SequentialExecutor;
import com.varmateo.yawg.actor.SequentialExecutorTestUtils.Pojo;


/**
 *
 */
public final class SequentialExecutorIT {


    /**
     *
     */
    @Test
    public void givenNSeqExecutors_whenTasksSubmittedToAll_thenTasksExecuteInParallel()
            throws Exception {

        int actionDuration = 100;
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
