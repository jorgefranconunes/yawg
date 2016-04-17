/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlReader;

import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.YawgException;


/**
 *
 */
/* package private */ final class DirBakerConfDao
        extends Object {


    private static final String FILE_NAME = ".yawg.yml";

    private static final String PARAM_TEMPLATE_NAME = "template";

    private static final Charset UTF8 = Charset.forName("UTF-8");


    /**
     *
     */
    public DirBakerConfDao() {

        // Nothing to do.
    }


    /**
     * Builds the baker configuration for the given directory,
     * inheriting the values from the given parent baker conf.
     *
     * @param parentConf The configuration from which we will inherit
     * values.
     *
     * @param sourceDir The directory for which we are going to create
     * the baker conf.
     *
     * @return The bake configuration for the given source directory.
     *
     * @throws YawgException Thrown if the configuration file could
     * not be read or had syntax problems.
     */
    public DirBakerConf load(final Path sourceDir)
            throws YawgException {

        Path confFile = sourceDir.resolve(FILE_NAME);
        Map<String,Object> items = loadYaml(confFile);
        DirBakerConf.Builder builder = new DirBakerConf.Builder();

        String templateName = getString(items, PARAM_TEMPLATE_NAME);
        if ( templateName != null ) {
            builder.setTemplateName(templateName);
        }

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     *
     */
    private Map<String,Object> loadYaml(final Path confFile)
            throws YawgException{

        Map<String,Object> result = null;

        try {
            if ( Files.isRegularFile(confFile) ) {
                result = doLoadYaml(confFile);
            } else {
                // Config file does not exist. We will return an empty
                // config set.
                result = Collections.emptyMap();
            }
        } catch ( IOException e) {
            YawgException.raise(
                    e,
                    "Failed to read config file \"{0}\" - {1} - {2}",
                    confFile,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        return result;
    }


    /**
     *
     */
    private Map<String,Object> doLoadYaml(final Path confFile)
            throws IOException {

        Map<String,Object> result = null;

        try ( Reader reader = Files.newBufferedReader(confFile, UTF8) ) {
            YamlReader yamlReader = new YamlReader(reader);
            Object yamlObj = yamlReader.read();

            if ( (yamlObj!=null) && (yamlObj instanceof Map) ) {
                result = (Map<String,Object>)yamlObj;
            } else {
                // The contents of the YAML file are invalid.
                result = Collections.emptyMap();
            }
        }

        return result;
    }


    /**
     *
     */
    private String getString(
            final Map<String,Object> items,
            final String key)
            throws YawgException {

        String result = (String)getWithType(items, key, String.class);

        return result;
    }


    /**
     *
     */
    private Object getWithType(
            final Map<String,Object> items,
            final String key,
            final Class<?> klass)
            throws YawgException {

        Object value = items.get(key);

        if ( value != null ) {
            if ( !klass.isInstance(value) ) {
                YawgException.raise(
                        "Invalid value {1} field \"{0}\"",
                        key,
                        klass.getSimpleName());
            }
        }

        return value;
    }


}
