/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.util.Lists;


/**
 * A factory o <code>DirBakeListener</code> instances.
 */
public interface DirBakeListenerFactory {


    /**
     * Returns a <code>DirBakeListener</code> object.
     */
    DirBakeListener newDirBakeListener();


    /**
     * Retrieves all <code>DirBakeListener</code> instances available
     * through the standard Java service loading facility
     * (https://docs.oracle.com/javase/tutorial/ext/basics/spi.html).
     *
     * @return All available <code>DirBakeListener</code> instances.
     */
    static Collection<DirBakeListenerFactory> getAllFactories() {

        ServiceLoader<DirBakeListenerFactory> loader =
                ServiceLoader.load(DirBakeListenerFactory.class);
        Iterator<DirBakeListenerFactory> allFactories = loader.iterator();
        Collection<DirBakeListenerFactory> result = Lists.newList(allFactories);

        return result;
    }


}
