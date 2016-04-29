/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.util.Holder;


/**
 *
 */
public final class HolderTest
        extends Object {


    /**
     *
     */
    @Test
    public void get() {

        Object myObject = new Object();
        MySupplier supplier = new MySupplier(myObject);
        Holder<Object> holder = Holder.of(supplier::getValue);

        assertEquals(0, supplier.getCallCount());

        assertSame(myObject, holder.get());
        assertEquals(1, supplier.getCallCount());

        assertSame(myObject, holder.get());
        assertEquals(1, supplier.getCallCount());
    }


    /**
     *
     */
    private static final class MySupplier
            extends Object {


        private final Object _value;
        private int _callCount = 0;


        /**
         *
         */
        public MySupplier(final Object value) {
            _value = value;
        }


        /**
         *
         */
        public Object getValue() {
            ++_callCount;
            return _value;
        }


        /**
         *
         */
        public int getCallCount() {
            return _callCount;
        }


    }


}