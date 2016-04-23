/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Utility functions for manipulating lists.
 */
public final class Lists
    extends Object {


    /**
     *
     */
    private Lists() {
    }


    /**
     *
     */
    public static <T> List<T> readOnlyCopy(
            final Collection<T> inputCollection) {

        List<T> copy = new ArrayList<T>(inputCollection);
        List<T> result = Collections.unmodifiableList(copy);

        return result;
    }


}





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

