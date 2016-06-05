/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util.yaml;

import java.io.Reader;
import java.util.Collections;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.util.yaml.YamlMap;


/**
 * A simple YAML parser.
 */
public final class YamlParser
        extends Object {


    /**
     *
     */
    public YamlParser() {

        // Nothing do to.
    }


    /**
     * Parses YAML contants from the given reader.
     *
     * @param reader Source of YAML contents to be read.
     *
     * @exception YawgException Thrown if there were any problems
     * reading from the reader, or if the YAML contents are invalid.
     */
    public YamlMap parse(final Reader reader)
            throws YawgException {

        YamlMap result = null;

        try {
            result = doParse(reader);
        } catch ( YamlException e ) {
            YawgException.raise(
                    e,
                    "Failed to parse YAML content - {0} - {1}",
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        return result;
    }


    /**
     *
     */
    private YamlMap doParse(final Reader reader)
            throws YamlException {

        Map<String,Object> resultMap = null;
        YamlReader yamlReader = new YamlReader(reader);
        Object yamlObj = yamlReader.read();

        if ( (yamlObj!=null) && (yamlObj instanceof Map) ) {
            resultMap = (Map<String,Object>)yamlObj;
        } else {
            // The contents of the YAML file are invalid.
            resultMap = Collections.emptyMap();
        }

        YamlMap result = new YamlMap(resultMap);

        return result;
    }


}
