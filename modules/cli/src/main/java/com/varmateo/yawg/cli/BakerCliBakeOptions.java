/**************************************************************************
 *
 * Copyright (c) 2019-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.nio.file.Path;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.immutables.value.Value.Immutable;

@Immutable
/* default */ interface BakerCliBakeOptions {
    Path sourceDir();
    Path targetDir();
    Option<Path> templatesDir();
    Map<String, String> externalPageVars();

    static Builder builder() {
        return new Builder();
    }

    static final class Builder extends ImmutableBakerCliBakeOptions.Builder {}

}
