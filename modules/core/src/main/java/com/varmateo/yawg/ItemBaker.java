/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;

import com.varmateo.yawg.YawgException;


/**
 *
 */
/* package private */ interface ItemBaker {


    /**
     * The baker name, used for informational purposes.
     */
    String getShortName();


    /**
     * Checks if it is able to bake the given path.
     */
    boolean isBakeable(Path path);


    /**
     *
     */
    void bake(
            Path sourcePath,
            Path targetDir)
            throws YawgException;
}