/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.nio.file.Path;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.inferred.freebuilder.FreeBuilder;


/**
 *
 */
@FreeBuilder
/* default */ interface BakerCliBakeOptions {


    Path sourceDir();
    Path targetDir();
    Option<Path> assetsDir();
    Option<Path> templatesDir();
    Map<String, String> externalPageVars();


    /**
     *
     */
    static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    final class Builder extends BakerCliBakeOptions_Builder {

        private Builder() {
            assetsDir(Option.none());
        }

    }


}
