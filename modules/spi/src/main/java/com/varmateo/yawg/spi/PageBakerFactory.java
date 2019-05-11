/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;


/**
 * A factory of <code>PageBaker</code> instances.
 *
 * <p>Concrete implementations of this interface are not required to
 * be thread safe.</p>
 *
 * <p>Instances of <code>PageBaker</code> created by concrete
 * implementations of this interface are not required to be thread
 * safe.</p>
 */
public interface PageBakerFactory {


    /**
     * Returns a <code>PageBaker</code> object.
     *
     * <p>You should not assume that a distinct object is returned for
     * every call. Nevertheless, implementors of this interface must
     * guarantee that the returned object is thread safe.</p>
     *
     * @return A new <code>PageBaker</code> object.
     */
    PageBaker createPageBaker();


}
