/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.varmateo.yawg.util.GlobMatcher;


/**
 *
 */
/* package private */ final class TemplateNameMatcher
        extends Object {


    private final Map<String,GlobMatcher> _matchersByName;


    /**
     * Initializes an empty template name matcher.
     */
    TemplateNameMatcher() {

        _matchersByName = Collections.emptyMap();
    }


    /**
     *
     */
    private TemplateNameMatcher(final Builder builder) {

        _matchersByName = new HashMap<>(builder._matchersByName);
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
    public Optional<String> getTemplateNameFor(final Path path) {

        Optional<String> name =
                _matchersByName.entrySet().stream()
                .filter(entry -> entry.getValue().test(path))
                .findFirst()
                .map(entry -> entry.getKey());

        return name;
    }


    /**
     *
     */
    public static final class Builder
            extends Object {


        private final Map<String,GlobMatcher> _matchersByName;


        /**
         *
         */
        private Builder() {

            _matchersByName = new HashMap<>();
        }


        /**
         *
         */
        public Builder addTemplateName(
                final String templateName,
                final GlobMatcher globMatcher) {

            _matchersByName.put(templateName, globMatcher);

            return this;
        }


        /**
         *
         */
        public TemplateNameMatcher build() {

            TemplateNameMatcher result = new TemplateNameMatcher(this);

            return result;
        }


    }


}
