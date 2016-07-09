/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

import com.varmateo.yawg.util.Lists;


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
     */
    Baker newBaker();


    /**
     * Retrieves all <code>BakerFactory</code> instances available
     * through the standard Java service loading facility
     * (https://docs.oracle.com/javase/tutorial/ext/basics/spi.html).
     */
    static Collection<BakerFactory> getAllFactories() {

        ServiceLoader<BakerFactory> loader =
                ServiceLoader.load(BakerFactory.class);
        Iterator<BakerFactory> allFactories = loader.iterator();
        Collection<BakerFactory> result = Lists.newList(allFactories);

        return result;
    }


}
