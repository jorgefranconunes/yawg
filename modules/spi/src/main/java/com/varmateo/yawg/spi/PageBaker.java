/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.nio.file.Path;


/**
 * A baker of files.
 */
public interface PageBaker {


    /**
     * The baker identifier. It is used to uniquely identify a baker.
     *
     * @return The baker name.
     */
    String shortName();


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
     * <p>The target directory must already exist. Otherwise a
     * failedresult will be returned.</p>
     *
     * @param sourcePath The file to be baked.
     *
     * @param context Data for performing the baking.
     *
     * @param targetDir The directory where the result of the bake
     * will be created.
     *
     * @return A result signaling success of failure.
     */
    PageBakeResult bake(
            Path sourcePath,
            PageContext context,
            Path targetDir);
}
