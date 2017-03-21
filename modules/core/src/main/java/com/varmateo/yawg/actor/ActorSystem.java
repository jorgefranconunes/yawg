/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.actor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import com.varmateo.yawg.actor.ActorFactory;


/**
 *
 */
public final class ActorSystem {


    private final Executor _executor;


    /**
     *
     */
    public ActorSystem(final Executor executor) {

        _executor = executor;
    }


    /**
     *
     */
    public <T> T createActor(
            final Class<T> actorType,
            final ActorFactory<T> actorFactory) {

        ActorRef actorRef = new ActorRef(_executor);
        T actorCore = actorFactory.newActorCore(actorRef);
        InvocationHandler proxyInvocationHandler =
                new ProxyActorInvocationHandler(actorCore, actorRef);

        @SuppressWarnings("unchecked")
        T actor =
                (T)Proxy.newProxyInstance(
                        actorType.getClassLoader(),
                        new Class<?>[] { actorType },
                        proxyInvocationHandler);

        return actor;
    }


    /**
     *
     */
    private static final class ProxyActorInvocationHandler
            implements InvocationHandler {


        private final Object _actorCore;
        private final ActorRef _actorRef;


        /**
         *
         */
        public ProxyActorInvocationHandler(
                final Object actorCore,
                final ActorRef actorRef) {

            _actorCore = actorCore;
            _actorRef = actorRef;
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

            _actorRef.perform(task);

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

            _actorRef.perform(task);

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
