/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.util.GlobMatcher;
import com.varmateo.yawg.util.yaml.YamlList;
import com.varmateo.yawg.util.yaml.YamlMap;
import com.varmateo.yawg.util.yaml.YamlParser;


/**
 * Reads the baker configuration for one given directory.
 */
/* package private */ final class DirBakerConfDao
        extends Object {


    private static final String CONF_FILE_NAME = ".yawg.yml";

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
    public DirBakerConf loadFromDir(final Path sourceDir)
            throws YawgException {

        DirBakerConf result = null;
        Path confFile = sourceDir.resolve(CONF_FILE_NAME);

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
                Files.newBufferedReader(confFile, StandardCharsets.UTF_8) ) {
            result = read(reader);
        }

        return result;
    }


    /**
     *
     */
    /* package private */ DirBakerConf read(final Reader reader)
            throws YawgException {

        YamlMap map = new YamlParser().parse(reader);
        DirBakerConf.Builder builder = new DirBakerConf.Builder();

        String templateName = map.getString(PARAM_TEMPLATE_NAME);
        if ( templateName != null ) {
            builder.setTemplateName(templateName);
        }

        Collection<GlobMatcher> filesToIgnore =
                getPatternList(map, PARAM_IGNORE);
        if ( filesToIgnore != null ) {
            builder.setFilesToIgnore(filesToIgnore);
        }

        Collection<GlobMatcher> filesToIncludeOnly =
                getPatternList(map, PARAM_INCLUDE_ONLY);
        if ( filesToIncludeOnly != null ) {
            builder.setFilesToIncludeOnly(filesToIncludeOnly);
        }

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     *
     */
    private List<GlobMatcher> getPatternList(
            final YamlMap map,
            final String key)
            throws YawgException {

        List<GlobMatcher> result = null;
        YamlList<String> itemList = map.getList(key, String.class);

        if ( itemList != null ) {
            result = new ArrayList<>();

            for ( int i=0, count=itemList.size(); i<count; ++i ) {
                String glob = itemList.getString(i);
                GlobMatcher pattern = null;

                try {
                    pattern = new GlobMatcher(glob);
                } catch ( PatternSyntaxException e ) {
                    YawgException.raise(
                            e,
                            "Invalid glob \"{0}\" on item {1} of {2}",
                            glob,
                            i,
                            key);
                }

                result.add(pattern);
            }
        }

        return result;
    }

}
