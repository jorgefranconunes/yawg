/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;


/**
 * A factory of <code>Baker</code> instances.
 *
 * <p>Concrete implementations of this interface are not required to
 * be thread safe.</p>
 *
 * <p>Instances of <code>BakerService</code> created by concrete
 * implementations of this interface are not required to be thread
 * safe.</p>
 */
public interface BakerServiceFactory {


    /**
     * Returns a <code>BakerService</code> object.
     *
     * <p>You should not assume that a distinct object is returned for
     * every call. Nevertheless, implementors of this interface must
     * guarantee that the returned object is thread safe.</p>
     *
     * @return A new <code>BakerService</code> object.
     */
    BakerService newBakerService();


}
