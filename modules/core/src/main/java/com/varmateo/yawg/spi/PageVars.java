/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;


/**
 * Set of variables made available to the template when generating the
 * final bake result.
 *
 * <p>The meaning of this variables is template specific.</p>
 */
public interface PageVars {


    /**
     * Retrieves the value of one of the variables.
     *
     * @param key The name of the variable whose value is to be
     * returned.
     *
     * @return An <code>Optional</code> containing the value of the
     * given variable, or nan empty <code>Optional</code> if the
     * variable does not exist.
     */
    Optional<Object> get(String key);


    /**
     * Fetches a view of the set of page variables as an unmodifiable
     * map.
     *
     * @return An unmodifiable map containing all the vars. Each entry
     * corresponds to one page variable.
     */
    Map<String, Object> asMap();


    /**
     * Creates an empty <code>PageVars</code>.
     *
     * @return An empty <code>PageVars</code>.
     */
    static PageVars empty() {

        return new PageVars() {
            @Override
            public Optional<Object> get(final String key) {
                return Optional.empty();
            }

            @Override
            public Map<String, Object> asMap() {
                return Collections.emptyMap();
            }
        };
    }

}
