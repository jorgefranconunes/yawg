/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import com.varmateo.yawg.util.GlobMatcher;


/**
 *
 */
/* default */ final class TemplateNameMatcher {


    private final Map<String,GlobMatcher> _matchersByName;


    /**
     *
     */
    private TemplateNameMatcher(final Builder builder) {

        _matchersByName = builder._matchersByName;
    }


    /**
     * Initializes an empty template name matcher.
     */
    public static TemplateNameMatcher empty() {

        return builder().build();
    }


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    public Option<String> templateNameFor(final Path path) {

        return _matchersByName
                .filter(entry -> entry._2().test(path))
                .headOption()
                .map(Tuple2::_1);
    }


    /**
     *
     */
    public static final class Builder {


        private Map<String,GlobMatcher> _matchersByName;


        /**
         *
         */
        private Builder() {

            _matchersByName = HashMap.empty();
        }


        /**
         *
         */
        public Builder addTemplateName(
                final String templateName,
                final GlobMatcher globMatcher) {

            _matchersByName = _matchersByName.put(templateName, globMatcher);

            return this;
        }


        /**
         *
         */
        public TemplateNameMatcher build() {

            return new TemplateNameMatcher(this);
        }


    }


}
