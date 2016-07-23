/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;


/**
 * A factory of <code>Baker</code> instances.
 *
 * <p>Concrete implementations of this interface are not required to
 * be thread safe.</p>
 *
 * <p>Instances of <code>Baker</code> created by concrete
 * implementations of this interface are not required to be thread
 * safe.</p>
 */
public interface BakerFactory {


    /**
     * Returns a <code>Baker</code> object.
     *
     * <p>You should not assume that a distinct object is returned for
     * every call. Nevertheless, implementors of this interface must
     * guarantee that the returned object is thread safe.</p>
     *
     * @return A new <code>Baker</code> object.
     */
    Baker newBaker();


}
