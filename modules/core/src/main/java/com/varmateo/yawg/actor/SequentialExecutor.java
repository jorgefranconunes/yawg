/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * An <code>Executor</code> that makes every submitted task be
 * executed sequentially, never concurrently.
 *
 * <p>The task execution is delegated to another <code>Executor</code>
 * received at construction time. This means that different tasks may
 * be executed in different threads. But it is guaranteed that no two
 * tasks will ever be executing concurrently.</p>
 *
 * <p>Why do we need this instead of just using a an executor from
 * <code>Executors.singleThreadExecutor()</code>? Because it allows to
 * serialize executions in multiple pipeline (one
 * <code>SequentialExecutor</code> per pipeline) without requiring one
 * thread per each pipeline.</p>
 */
final class SequentialExecutor
        implements Executor {


    private final AtomicBoolean _lock;
    private final ConcurrentLinkedQueue<Runnable> _mailbox;
    private final Executor _delegateExecutor;


    /**
     *
     */
    SequentialExecutor(final Executor delegateExecutor) {

        _lock = new AtomicBoolean(true);
        _mailbox =  new ConcurrentLinkedQueue<>();
        _delegateExecutor = delegateExecutor;
    }


    /**
     * Schedules a task to be executed sometime in the future.
     *
     * @param task The task to be executed.
     */
    @Override
    public void execute(final Runnable task) {

        _mailbox.offer(task);
        scheduleTaskProcessing();
    }


    /**
     *
     */
    private void scheduleTaskProcessing() {

        if ( !_mailbox.isEmpty() && _lock.compareAndSet(true, false) ) {
            // At this point we know there are tasks to process
            // and there is no ongoing thread processing a
            // task.
            _delegateExecutor.execute(this::processTask);
        }
    }


    /**
     * This method is always executed within the delegate executor.
     */
    private void processTask() {

        Runnable task = _mailbox.poll();

        if ( task != null ) {
            try {
                task.run();
            } finally {
                // Continue with the next task, in case there are more.
                _delegateExecutor.execute(this::processTask);
            }
        } else {
            // Clear the lock.
            _lock.set(true);

            // In case a task has meanwhile been queued while we were
            // holding the lock.
            scheduleTaskProcessing();
        }
    }


}
