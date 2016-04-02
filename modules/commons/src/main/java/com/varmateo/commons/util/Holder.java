/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.util;

import java.util.function.Supplier;


/**
 * A memoizer for a single object.
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
    private synchronized T buildAndCache(final Supplier<T> builder) {

        if ( !_isBuilt ) {
            T payload = builder.get();

            _getter  = () -> payload;
            _isBuilt = true;
        }

        return _getter.get();
    }


    /**************************************************************************
     *
     */
    public T get() {

        return _getter.get();
    }


    /**
     *
     */
    public static <T> Holder<T> of(final Supplier<T> builder) {

        Holder<T> result = new Holder<T>(builder);

        return result;
    }


}
