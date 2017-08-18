/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.util.concurrent.Executor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.varmateo.yawg.actor.ActorRef;
import com.varmateo.yawg.actor.SequentialExecutor;


/**
 * An internal implementation of <code>ActorRef</code> intended to be
 * used solely by the <code>ActorSystem</code> implementation.
 */
/* default */ final class ActorRefImpl<T>
        implements ActorRef<T> {


    private final Executor _sequentialExecutor;
    private final ProxyActorInvocationHandler _proxyInvocationHandler;
    private final T _actorSelf;


    /**
     *
     * @param actorType The class object of the interface the actor
     * instance implements.
     *
     * @param executor Used for executing actors methods.
     */
    /* default */ ActorRefImpl(
            final Class<T> actorType,
            final Executor executor) {

        Executor sequentialExecutor = new SequentialExecutor(executor);
        ProxyActorInvocationHandler proxyInvocationHandler =
                new ProxyActorInvocationHandler(sequentialExecutor);

        @SuppressWarnings("unchecked")
        T actorSelf =
                (T)Proxy.newProxyInstance(
                        actorType.getClassLoader(),
                        new Class<?>[] { actorType },
                        proxyInvocationHandler);

        _sequentialExecutor = sequentialExecutor;
        _proxyInvocationHandler = proxyInvocationHandler;
        _actorSelf = actorSelf;
    }


    /**
     * Completes the initialization of the <code>ActorRef</code>. From
     * here on the object returned by the <code>{@link #self()}</code>
     * method will perform as desired.
     *
     * @param actorCore The object being wrapped by this
     * <code>ActorRef</code>.
     */
    /* default */ void setActorCore(final T actorCore) {

        _proxyInvocationHandler.setActorCore(actorCore);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void perform(final Runnable action) {

        _sequentialExecutor.execute(action);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public T self() {

        return _actorSelf;
    }


    /**
     *
     */
    private static final class ProxyActorInvocationHandler
            implements InvocationHandler {


        private Object _actorCore;
        private final Executor _executor;


        /**
         *
         */
        ProxyActorInvocationHandler(final Executor executor) {

            _executor = executor;
        }


        /**
         *
         */
        public void setActorCore(final Object actorCore) {

            _actorCore = actorCore;
        }


        /**
         *
         */
        @Override
        public Object invoke(
                final Object proxy,
                final Method method,
                final Object[] args)
                throws Throwable {

            if ( method.getReturnType() == Void.TYPE ) {
                return invokeVoidMethod(method, args);
            } else {
                return invokeNonVoidMethod(method, args);
            }
        }


        /**
         *
         */
        private Object invokeVoidMethod(
                final Method method,
                final Object[] args) {

            Runnable task = () -> doInvokeVoidMethod(_actorCore, method, args);

            _executor.execute(task);

            return null;
        }


        /**
         *
         */
        private static void doInvokeVoidMethod(
                final Object actorCore,
                final Method method,
                final Object[] args) {

            try {
                method.invoke(actorCore, args);
            } catch ( IllegalAccessException e ) {
                throw new RuntimeException(e);
            } catch ( InvocationTargetException e ) {
                Throwable cause = e.getCause();
                if ( cause instanceof RuntimeException ) {
                    throw (RuntimeException)cause;
                } else {
                    throw new RuntimeException(cause);
                }
            }
        }


        /**
         *
         */
        private Object invokeNonVoidMethod(
                final Method method,
                final Object[] args)
                throws Throwable {

            CompletableFuture<Object> future = new CompletableFuture<>();
            Runnable task =
                    () -> doInvokeNonVoidMethod(_actorCore, method, args,future);

            _executor.execute(task);

            final Object result;

            try {
                result = future.join();
            } catch ( CompletionException e ) {
                Throwable cause = e.getCause();
                throw cause;
            }

            return result;
        }


        /**
         *
         */
        private static void doInvokeNonVoidMethod(
                final Object actorCore,
                final Method method,
                final Object[] args,
                final CompletableFuture<Object> future) {

            try {
                Object result = method.invoke(actorCore, args);
                future.complete(result);
            } catch ( IllegalAccessException e ) {
                future.completeExceptionally(new RuntimeException(e));
            } catch ( InvocationTargetException e ) {
                future.completeExceptionally(e.getCause());
            }
        }


    }


}
