/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.YawgException;


/**
 * A baker of files.
 */
public interface Baker {


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
     * @param context Data for performing the baking.
     *
     * @param targetDir The directory where the result of the bake
     * will be created.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    void bake(
            Path sourcePath,
            PageContext context,
            Path targetDir)
            throws YawgException;
}
