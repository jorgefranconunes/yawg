/**************************************************************************
 *
 * Copyright (c) 2019-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.nio.file.Path;
import java.util.Map;

import org.immutables.value.Value.Immutable;

/**
 * Set of configuration parameters for baking a site using a {@code
 * SiteBaker} instance.
 */
@Immutable
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
        return builder().from(data);
    }

    /**
     * A builder of {@code BakeOptions} instances.
     */
    static final class Builder extends ImmutableBakeOptions.Builder {}

}
