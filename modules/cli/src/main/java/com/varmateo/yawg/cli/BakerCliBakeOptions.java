/**************************************************************************
 *
 * Copyright (c) 2019-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.nio.file.Path;
import java.util.Optional;

import io.vavr.collection.Map;
import org.immutables.value.Value.Immutable;

@Immutable
interface BakerCliBakeOptions {
    Path sourceDir();

    Path targetDir();

    Optional<Path> templatesDir();

    Map<String, String> externalPageVars();

    static Builder builder() {
        return new Builder();
    }

    static final class Builder extends ImmutableBakerCliBakeOptions.Builder {}

}
