/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;


/**
 * Set of configuration parameters for baking a site with a
 * <code>SiteBaker</code> instance.
 */
public interface BakeOptions {


    /**
     * Path of directory containing the assets files. These files will
     * be copied without any changes to the target directory.
     *
     * <p>An empty <code>Optional</code> means no assets will be
     * copied.</p>
     */
    Optional<Path> assetsDir();


    /**
     * Path of directory containing the documents to be baked.
     */
    Path sourceDir();


    /**
     * Path of directory where the results of the baking will be
     * stored.
     */
    Path targetDir();


    /**
     * Set of page variables provided externally.
     *
     * <p>Page variables are intended to be used by templates.</p>
     *
     * @return A read-only map containing the page variables. Keys in
     * the map are the variable names.
     */
    Map<String, Object> externalPageVars();

}
