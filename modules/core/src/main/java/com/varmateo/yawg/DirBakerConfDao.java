/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.util.Charsets;


/**
 * Reads the baker configuration for one given directory.
 */
/* package private */ final class DirBakerConfDao
        extends Object {


    private static final String FILE_NAME = ".yawg.yml";

    private static final String PARAM_TEMPLATE_NAME = "template";
    private static final String PARAM_IGNORE = "ignore";
    private static final String PARAM_INCLUDE_ONLY = "includeOnly";


    /**
     *
     */
    public DirBakerConfDao() {

        // Nothing to do.
    }


    /**
     * Builds the baker configuration for the given directory.
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

        DirBakerConf result = null;
        Path confFile = sourceDir.resolve(FILE_NAME);

        if ( Files.isRegularFile(confFile) ) {
            result = loadFromFile(confFile);
        } else {
            result = new DirBakerConf.Builder().build();
        }

        return result;
    }


    /**
     *
     */
    /* package private */ DirBakerConf loadFromFile(final Path confFile)
            throws YawgException {

        DirBakerConf result = null;

        try {
            result = doLoadFromFile(confFile);
        } catch ( IOException e ) {
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
    private DirBakerConf doLoadFromFile(final Path confFile)
            throws YawgException, IOException {

        DirBakerConf result = null;

        try ( Reader reader =
                Files.newBufferedReader(confFile, Charsets.UTF_8) ) {
            result = read(reader);
        }

        return result;
    }


    /**
     *
     */
    /* package private */ DirBakerConf read(final Reader reader)
            throws YawgException, IOException {

        Map<String,Object> items = readYaml(reader);
        DirBakerConf.Builder builder = new DirBakerConf.Builder();

        String templateName = getString(items, PARAM_TEMPLATE_NAME);
        if ( templateName != null ) {
            builder.setTemplateName(templateName);
        }

        Collection<Pattern> filesToIgnore =
                getPatternList(items, PARAM_IGNORE);
        if ( filesToIgnore != null ) {
            builder.addFilesToIgnore(filesToIgnore);
        }

        Collection<Pattern> filesToIncludeOnly =
                getPatternList(items, PARAM_INCLUDE_ONLY);
        if ( filesToIncludeOnly != null ) {
            builder.setFilesToIncludeOnly(filesToIncludeOnly);
        }

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     *
     */
    private Map<String,Object> readYaml(final Reader reader)
            throws YamlException {

        Map<String,Object> result = null;
        YamlReader yamlReader = new YamlReader(reader);
        Object yamlObj = yamlReader.read();

        if ( (yamlObj!=null) && (yamlObj instanceof Map) ) {
            result = (Map<String,Object>)yamlObj;
        } else {
            // The contents of the YAML file are invalid.
            result = Collections.emptyMap();
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
    private List<Pattern> getPatternList(
            final Map<String,Object> items,
            final String key)
            throws YawgException {

        List<Pattern> result = null;
        List<Object> itemList = (List)getWithType(items, key, List.class);

        if ( itemList != null ) {
            result = new ArrayList<>();

            for ( int i=0, count=itemList.size(); i<count; ++i ) {
                String regex = (String)getWithType(itemList, i, String.class);
                Pattern pattern = null;

                try {
                    pattern = Pattern.compile(regex);
                } catch ( PatternSyntaxException e ) {
                    YawgException.raise(
                            e,
                            "Invalid regex \"{0}\" on item {1} of {2}",
                            regex,
                            i,
                            key);
                }

                result.add(pattern);
            }
        }

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

        if ( (value!=null) && !klass.isInstance(value) ) {
            YawgException.raise(
                    "Invalid {2} value in {1} field \"{0}\"",
                    key,
                    klass.getSimpleName(),
                    value.getClass().getSimpleName());
        }

        return value;
    }


    /**
     *
     */
    private Object getWithType(
            final List<Object> items,
            final int index,
            final Class<?> klass)
            throws YawgException {

        Object value = items.get(index);

        if ( (value==null) || !klass.isInstance(value) ) {
            YawgException.raise(
                    "Invalid {2} value in {1} position {0}",
                    index,
                    klass.getSimpleName(),
                    (value==null) ? "NULL" : value.getClass().getSimpleName());
        }

        return value;
    }

}
