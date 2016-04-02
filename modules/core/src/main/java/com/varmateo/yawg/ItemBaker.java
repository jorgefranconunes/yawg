/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Optional;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.YawgTemplate;


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
     * Bakes the given file into the specified target directory.
     *
     * <p>The target directory must already exist. Otherwise an
     * exception will be thrown.</p>
     *
     * @param sourcePath The file to be baked.
     *
     * @param template Used for generating the target document.
     *
     * @param targetDir The directory where the result of the bake
     * will be created.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     *
     */
    void bake(
            Path sourcePath,
            Optional<YawgTemplate> template,
            Path targetDir)
            throws YawgException;
}
