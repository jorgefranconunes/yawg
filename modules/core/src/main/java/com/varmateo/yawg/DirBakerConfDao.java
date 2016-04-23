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
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
    private static final String PARAM_FILES_TO_IGNORE = "ignore";

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
        Collection<Pattern> filesToIgnore =
                getPatternList(items, PARAM_FILES_TO_IGNORE);
        if ( filesToIgnore != null ) {
            builder.addFilesToIgnore(filesToIgnore);
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
    private List<Pattern> getPatternList(
            final Map<String,Object> items,
            final String key)
            throws YawgException {

        List<Pattern> result = new ArrayList<>();
        List<Object> itemList = (List)getWithType(items, key, List.class);

        if ( itemList != null ) {
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

        if ( value != null ) {
            if ( !klass.isInstance(value) ) {
                YawgException.raise(
                        "Invalid {2} value in {1} field \"{0}\"",
                        key,
                        klass.getSimpleName(),
                        value.getClass().getSimpleName());
            }
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
