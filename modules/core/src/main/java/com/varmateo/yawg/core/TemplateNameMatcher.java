/**************************************************************************
 *
 * Copyright (c) 2016-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Optional;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import com.varmateo.yawg.util.GlobMatcher;

/* package private */ final class TemplateNameMatcher {

    private final Map<String, GlobMatcher> _matchersByName;

    TemplateNameMatcher(final Builder builder) {
        _matchersByName = builder._matchersByName;
    }

    /**
     * Initializes an empty template name matcher.
     */
    public static TemplateNameMatcher empty() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<String> templateNameFor(final Path path) {
        return _matchersByName
                .filter(entry -> entry._2().test(path))
                .headOption()
                .map(Tuple2::_1)
                .toJavaOptional();
    }

    public static final class Builder {

        private Map<String, GlobMatcher> _matchersByName;

        Builder() {
            _matchersByName = HashMap.empty();
        }

        public Builder addTemplateName(
                final String templateName,
                final GlobMatcher globMatcher
        ) {
            _matchersByName = _matchersByName.put(templateName, globMatcher);

            return this;
        }

        public TemplateNameMatcher build() {
            return new TemplateNameMatcher(this);
        }
    }
}
