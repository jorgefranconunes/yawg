/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.control.Option;

import com.varmateo.yawg.util.GlobMatcher;


/**
 *
 */
/* package private */ final class TemplateNameMatcher {


    private final Map<String,GlobMatcher> _matchersByName;


    /**
     * Initializes an empty template name matcher.
     */
    TemplateNameMatcher() {

        _matchersByName = HashMap.of();
    }


    /**
     *
     */
    private TemplateNameMatcher(final Builder builder) {

        _matchersByName = builder._matchersByName;
    }


    /**
     *
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
    }


    /**
     *
     */
    public Option<String> getTemplateNameFor(final Path path) {

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

            _matchersByName = HashMap.of();
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
