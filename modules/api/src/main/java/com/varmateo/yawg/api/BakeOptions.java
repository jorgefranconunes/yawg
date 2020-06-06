/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.inferred.freebuilder.FreeBuilder;


/**
 * Set of configuration parameters for baking a site using a {@code
 * SiteBaker} instance.
 */
@FreeBuilder
public interface BakeOptions {


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


    /**
     * Creates a new empty builder.
     */
    static Builder builder() {

        return new Builder();
    }


    /**
     * Creates a new builder initialized with the attributes from the
     * given {@code BakeOptions}.
     */
    static Builder builder(final BakeOptions data) {

        return Builder.from(data);
    }


    /**
     * A builder of {@code BakeOptions} instances.
     */
    final class Builder extends BakeOptions_Builder {

        private Builder() {
            // Nothing to do.
        }

    }

}
