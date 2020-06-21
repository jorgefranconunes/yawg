/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlReader;

import com.varmateo.yawg.util.SimpleMap;


/**
 * A simple YAML parser.
 */
public final class YamlParser {


    /**
     * Parses YAML contants from the given reader.
     *
     * @param reader Source of YAML contents to be read.
     *
     * @return A map with the results of parsing the given YAML
     * content.
     *
     * @exception IOException Thrown if there were any problems
     * reading from the reader, or if the YAML contents are invalid.
     */
    public SimpleMap parse(final Reader reader)
            throws IOException {

        final Map<String, Object> resultMap;
        final YamlReader yamlReader = new YamlReader(reader);
        final Object yamlObj = yamlReader.read();

        if ( (yamlObj != null) && (yamlObj instanceof Map) ) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> map = (Map<String, Object>) yamlObj;
            resultMap = map;
        } else {
            // The contents of the YAML file are invalid.
            resultMap = Collections.emptyMap();
        }

        return new SimpleMap(resultMap);
    }


}
