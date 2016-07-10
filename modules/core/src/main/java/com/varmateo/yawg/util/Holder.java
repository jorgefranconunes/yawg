/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.function.Supplier;


/**
 * A memoizer for a single object.
 *
 * @param <T> The type of objects the holder will hold.
 */
public final class Holder<T>
    extends Object {


    private Supplier<T> _getter  = null;
    private boolean     _isBuilt = false;


    /**
     *
     */
    private Holder(final Supplier<T> builder) {

        _getter = () -> buildAndCache(builder);
    }


    /**
     *
     */
    private T buildAndCache(final Supplier<T> builder) {

        synchronized ( this ) {
            if ( !_isBuilt ) {
                T payload = builder.get();

                _getter  = () -> payload;
                _isBuilt = true;
            }
        }

        return _getter.get();
    }


    /**
     * Retrieves the object contained in this holder.
     *
     * <p>The first time this method is called, the
     * <code>Supplier</code> passed to the <code>{@link
     * #of(Supplier<T>)}</code> method will be invoked to fetch the
     * actual object. All following calls will then always return that
     * same object.</p>
     *
     * @return The object obtained from the initial
     * <code>Supplier</code> this holder was constructd from.
     */
    public T get() {

        return _getter.get();
    }


    /**
     * Creates a new holder whose content will be retrieved from the
     * given <code>Supplier</code>.
     *
     * @param builder The supplier from which the object contained in
     * the holder will be obtained.
     *
     * @return A newly created holder.
     *
     * @param <T> The type of object the returned holder will hold.
     */
    public static <T> Holder<T> of(final Supplier<T> builder) {

        Holder<T> result = new Holder<T>(builder);

        return result;
    }


}
