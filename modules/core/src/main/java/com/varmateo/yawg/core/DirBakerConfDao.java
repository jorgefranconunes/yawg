/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.PatternSyntaxException;

import javaslang.collection.Seq;
import javaslang.collection.Stream;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.TemplateNameMatcher;
import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.GlobMatcher;
import com.varmateo.yawg.util.SimpleMap;
import com.varmateo.yawg.util.SimpleList;
import com.varmateo.yawg.util.YamlParser;


/**
 * Reads the baker configuration for one given directory.
 */
/* package private */ final class DirBakerConfDao {


    private static final String CONF_FILE_NAME = ".yawg.yml";

    private static final String PARAM_BAKER_TYPES = "bakerTypes";
    private static final String PARAM_EXCLUDE = "exclude";
    private static final String PARAM_INCLUDE_HERE = "includeHere";
    private static final String PARAM_TEMPLATE = "template";
    private static final String PARAM_PAGE_VARS = "pageVars";
    private static final String PARAM_PAGE_VARS_HERE = "pageVarsHere";
    private static final String PARAM_TEMPLATES_HERE = "templatesHere";
    private static final String PARAM_EXTRA_DIRS_HERE = "extraDirsHere";


    /**
     *
     */
    /* package private */ DirBakerConfDao() {
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
            result = DirBakerConf.empty();
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

        SimpleMap confMap = new YamlParser().parse(reader);
        DirBakerConf.Builder builder = DirBakerConf.builder();

        confMap.getString(PARAM_TEMPLATE)
                .ifPresent(builder::setTemplateName);

        getPatternList(confMap, PARAM_EXCLUDE)
                .ifPresent(builder::setFilesToExclude);

        getPatternList(confMap, PARAM_INCLUDE_HERE)
                .ifPresent(builder::setFilesToIncludeHere);

        getBakerTypes(confMap, PARAM_BAKER_TYPES)
                .ifPresent(builder::setBakerTypes);

        getPageVars(confMap, PARAM_PAGE_VARS)
                .ifPresent(builder::setPageVars);

        getPageVars(confMap, PARAM_PAGE_VARS_HERE)
                .ifPresent(builder::setPageVarsHere);

        getTemplatesHere(confMap, PARAM_TEMPLATES_HERE)
                .ifPresent(builder::setTemplatesHere);

        getPathList(confMap, PARAM_EXTRA_DIRS_HERE)
                .ifPresent(builder::setExtraDirsHere);

        DirBakerConf result = builder.build();

        return result;
    }


    /**
     *
     */
    private Optional<GlobMatcher> getPatternList(
            final SimpleMap confMap,
            final String key)
            throws YawgException {

        return confMap.getList(key, String.class)
                .map(itemList ->
                     Stream.ofAll(itemList)
                     .zipWithIndex()
                     .foldLeft(
                             GlobMatcher.builder(),
                             (xs,x) -> addToGlobBuilder(xs, x._1(), x._2(), key))
                     .build());
    }


    private static GlobMatcher.Builder addToGlobBuilder(
            final GlobMatcher.Builder builder,
            final String glob,
            final Long index,
            final String key) {

        try {
            return builder.addGlobPattern(glob);
        } catch ( PatternSyntaxException e ) {
            throw Exceptions.raise(
                    e,
                    "Invalid glob \"{0}\" on item {1} of {2}",
                    glob,
                    index,
                    key);
        }
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
            final SimpleMap confMap,
            final String key)
            throws YawgException {

        return confMap
                .getMap(key)
                .map(pageVarsMap ->
                     PageVars.builder(pageVarsMap.asMap()).build());
    }


    /**
     *
     */
    private Optional<TemplateNameMatcher> getTemplatesHere(
            final SimpleMap map,
            final String key)
            throws YawgException {

        Optional<TemplateNameMatcher> result;
        Optional<SimpleMap> templatesHereMapOpt = map.getMap(key);

        if ( templatesHereMapOpt.isPresent() ) {
            SimpleMap templatesHereMap = templatesHereMapOpt.get();
            TemplateNameMatcher.Builder builder = TemplateNameMatcher.builder();

            for ( String templateName : templatesHereMap.keySet() ) {
                GlobMatcher globMatcher =
                        getPatternList(templatesHereMap, templateName).get();

                builder.addTemplateName(templateName, globMatcher);
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
    private Optional<Seq<Path>> getPathList(
            final SimpleMap confMap,
            final String key)
            throws YawgException {

        return confMap
                .getList(key, String.class)
                .map(itemList ->
                     Stream.ofAll(itemList)
                     .zipWithIndex()
                     .map(tuple -> buildPath(tuple._1(), tuple._2(), key)));
    }


    private static Path buildPath(
            final String pathStr,
            final Long index,
            final String key) {

        try {
            return Paths.get(pathStr);
        } catch ( InvalidPathException e ) {
            throw Exceptions.raise(
                    e,
                    "Invalid path \"{0}\" on item {1} of {2}",
                    pathStr,
                    index,
                    key);
        }
    }


}
