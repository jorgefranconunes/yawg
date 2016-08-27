/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.PatternSyntaxException;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.GlobMatcher;
import com.varmateo.yawg.util.SimpleMap;
import com.varmateo.yawg.util.SimpleList;
import com.varmateo.yawg.util.YamlParser;


/**
 * Reads the baker configuration for one given directory.
 */
/* package private */ final class DirBakerConfDao
        extends Object {


    private static final String CONF_FILE_NAME = ".yawg.yml";

    private static final String PARAM_BAKER_TYPES = "bakerTypes";
    private static final String PARAM_EXCLUDE = "exclude";
    private static final String PARAM_INCLUDE_HERE = "includeHere";
    private static final String PARAM_TEMPLATE = "template";
    private static final String PARAM_PAGE_VARS = "pageVars";
    private static final String PARAM_PAGE_VARS_HERE = "pageVarsHere";


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
            result = DirBakerConf.builder().build();
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
            Exceptions.raise(
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
            throws IOException, YawgException {

        SimpleMap map = new YamlParser().parse(reader);
        DirBakerConf.Builder builder = DirBakerConf.builder();

        map.getString(PARAM_TEMPLATE)
                .ifPresent(builder::setTemplateName);

        getPatternList(map, PARAM_EXCLUDE)
                .ifPresent(builder::setFilesToExclude);

        getPatternList(map, PARAM_INCLUDE_HERE)
                .ifPresent(builder::setFilesToIncludeHere);

        getBakerTypes(map, PARAM_BAKER_TYPES)
                .ifPresent(builder::setBakerTypes);

        getPageVars(map, PARAM_PAGE_VARS)
                .ifPresent(builder::setPageVars);

        getPageVars(map, PARAM_PAGE_VARS_HERE)
                .ifPresent(builder::setPageVarsHere);

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     *
     */
    private Optional<GlobMatcher> getPatternList(
            final SimpleMap map,
            final String key)
            throws YawgException {

        Optional<GlobMatcher> result;
        Optional<SimpleList<String>> itemListOpt =
                map.getList(key, String.class);

        if ( itemListOpt.isPresent() ) {
            SimpleList<String> itemList = itemListOpt.get();
            GlobMatcher.Builder builder = GlobMatcher.builder();

            for ( int i=0, count=itemList.size(); i<count; ++i ) {
                String glob = itemList.get(i);

                try {
                    builder.addGlobPattern(glob);
                } catch ( PatternSyntaxException e ) {
                    Exceptions.raise(
                            e,
                            "Invalid glob \"{0}\" on item {1} of {2}",
                            glob,
                            i,
                            key);
                }
            }
            result = Optional.of(builder.build());
        } else {
            result = Optional.empty();
        }

        return result;
    }


    /**
     *
     */
    private Optional<BakerMatcher> getBakerTypes(
            final SimpleMap map,
            final String key)
            throws YawgException {

        Optional<BakerMatcher> result;
        Optional<SimpleMap> bakerTypesMapOpt = map.getMap(key);

        if ( bakerTypesMapOpt.isPresent() ) {
            SimpleMap bakerTypesMap = bakerTypesMapOpt.get();
            BakerMatcher.Builder builder = BakerMatcher.builder();

            for ( String bakerType : bakerTypesMap.keySet() ) {
                getPatternList(bakerTypesMap, bakerType)
                        .ifPresent(m -> builder.addBakerType(bakerType, m));
            }
            result = Optional.of(builder.build());
        } else {
            result = Optional.empty();
        }

        return result;
    }


    /**
     *
     */
    private Optional<PageVars> getPageVars(
            final SimpleMap map,
            final String key)
            throws YawgException {

        Optional<PageVars> result;
        Optional<SimpleMap> pageVarsMapOpt = map.getMap(key);

        if ( pageVarsMapOpt.isPresent() ) {
            SimpleMap pageVarsMap = pageVarsMapOpt.get();
            PageVars pageVars =
                    PageVars.builder(pageVarsMap.asMap())
                    .build();

            result = Optional.of(pageVars);
        } else {
            result = Optional.empty();
        }

        return result;
    }

}
