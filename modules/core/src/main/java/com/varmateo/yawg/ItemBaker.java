/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Optional;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;


/**
 * A baker.
 */
public interface ItemBaker {


    /**
     * The baker identifier. It is used to uniquely identify a baker.
     *
     * @return The baker name.
     */
    String getShortName();


    /**
     * Checks if this baker is able to bake the given file.
     *
     * <p>Typical implementations check if the file extension is one
     * of a set of supported file types.</p>
     *
     * @param path The file system path of the file to be checked.
     *
     * @return True if this baker can handle the file. False
     * otherwise.
     */
    boolean isBakeable(Path path);


    /**
     * Bakes the given file creating the result in the specified
     * target directory.
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
     */
    void bake(
            Path sourcePath,
            Optional<PageTemplate> template,
            Path targetDir)
            throws YawgException;
}
