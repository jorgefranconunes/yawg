/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
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

import io.vavr.Function1;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.util.GlobMatcher;
import com.varmateo.yawg.util.SimpleMap;
import com.varmateo.yawg.util.YamlParser;


/**
 * Reads the baker configuration for one given directory.
 */
/* default */ final class DirBakeOptionsDao {


    private static final String CONF_FILE_NAME = ".yawg.yml";

    private static final String PARAM_BAKER_TYPES = "bakerTypes";
    private static final String PARAM_EXCLUDE = "exclude";
    private static final String PARAM_EXCLUDE_HERE = "excludeHere";
    private static final String PARAM_INCLUDE_HERE = "includeHere";
    private static final String PARAM_TEMPLATE = "template";
    private static final String PARAM_PAGE_VARS = "pageVars";
    private static final String PARAM_PAGE_VARS_HERE = "pageVarsHere";
    private static final String PARAM_TEMPLATES_HERE = "templatesHere";
    private static final String PARAM_EXTRA_DIRS_HERE = "extraDirsHere";


    public DirBakeOptionsDao() {
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
    public DirBakeOptions loadFromDir(final Path sourceDir)
            throws YawgException {

        final Path confFile = sourceDir.resolve(CONF_FILE_NAME);

        return Files.isRegularFile(confFile)
                ? loadFromFile(confFile)
                : DirBakeOptions.empty();
    }


    /**
     *
     */
    public DirBakeOptions loadFromFile(final Path confFile) {

        final DirBakeOptions result;

        try {
            result = doLoadFromFile(confFile);
        } catch ( IOException e ) {
            throw DirBakeOptionsDaoException.loadConfigFailure(confFile, e);
        }

        return result;
    }


    /**
     *
     */
    private DirBakeOptions doLoadFromFile(final Path confFile)
            throws IOException {

        final DirBakeOptions result;

        try ( Reader reader = Files.newBufferedReader(confFile, StandardCharsets.UTF_8) ) {
            result = read(reader);
        }

        return result;
    }


    /**
     *
     */
    public DirBakeOptions read(final Reader reader)
            throws IOException {

        final SimpleMap confMap = YamlParser.parse(reader);
        final DirBakeOptions.Builder builder = DirBakeOptions.builder();

        confMap.getString(PARAM_TEMPLATE)
                .ifPresent(builder::templateName);

        getPatternList(confMap, PARAM_EXCLUDE)
                .ifPresent(builder::filesToExclude);

        getPatternList(confMap, PARAM_EXCLUDE_HERE)
                .ifPresent(builder::filesToExcludeHere);

        getPatternList(confMap, PARAM_INCLUDE_HERE)
                .ifPresent(builder::filesToIncludeHere);

        getBakerTypes(confMap, PARAM_BAKER_TYPES)
                .ifPresent(builder::bakerTypes);

        getPageVars(confMap, PARAM_PAGE_VARS)
                .ifPresent(builder::pageVars);

        getPageVars(confMap, PARAM_PAGE_VARS_HERE)
                .ifPresent(builder::pageVarsHere);

        getTemplatesHere(confMap, PARAM_TEMPLATES_HERE)
                .ifPresent(builder::templatesHere);

        getPathList(confMap, PARAM_EXTRA_DIRS_HERE)
                .ifPresent(builder::extraDirsHere);

        return builder.build();
    }


    /**
     *
     */
    private Optional<GlobMatcher> getPatternList(
            final SimpleMap confMap,
            final String key)
            throws YawgException {

        final Function1<Seq<String>, GlobMatcher> itemsToGlobMatcher = items ->
                items
                .zipWithIndex()
                .foldLeft(
                        GlobMatcher.builder(),
                        (xs, x) -> addToGlobBuilder(xs, x._1, x._2, key))
                .build();

        return confMap.getList(key, String.class)
                .map(Stream::ofAll)
                .map(itemsToGlobMatcher);
    }


    private static GlobMatcher.Builder addToGlobBuilder(
            final GlobMatcher.Builder builder,
            final String glob,
            final Integer index,
            final String key) {

        try {
            return builder.addGlobPattern(glob);
        } catch ( PatternSyntaxException e ) {
            throw DirBakeOptionsDaoException.invalidGlob(glob, index, key);
        }
    }


    /**
     *
     */
    private Optional<BakerMatcher> getBakerTypes(
            final SimpleMap map,
            final String key)
            throws YawgException {

        final Optional<BakerMatcher> result;
        final Optional<SimpleMap> bakerTypesMapOpt = map.getMap(key);

        if ( bakerTypesMapOpt.isPresent() ) {
            final SimpleMap bakerTypesMap = bakerTypesMapOpt.get();
            final BakerMatcher.Builder builder = BakerMatcher.builder();

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
                     PageVarsBuilder.create(pageVarsMap.asMap()).build());
    }


    /**
     *
     */
    private Optional<TemplateNameMatcher> getTemplatesHere(
            final SimpleMap map,
            final String key)
            throws YawgException {

        final Optional<TemplateNameMatcher> result;
        final Optional<SimpleMap> templatesHereMapOpt = map.getMap(key);

        if ( templatesHereMapOpt.isPresent() ) {
            final SimpleMap templatesHereMap = templatesHereMapOpt.get();
            final TemplateNameMatcher.Builder builder = TemplateNameMatcher.builder();

            for ( String templateName : templatesHereMap.keySet() ) {
                final GlobMatcher globMatcher =
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
                .map(Stream::ofAll)
                .map(itemSeq ->
                     itemSeq
                     .zipWithIndex()
                     .map(tuple -> buildPath(tuple._1, tuple._2, key)));
    }


    private static Path buildPath(
            final String pathStr,
            final Integer index,
            final String key) {

        try {
            return Paths.get(pathStr);
        } catch ( InvalidPathException e ) {
            throw DirBakeOptionsDaoException.invalidPath(pathStr, index, key);
        }
    }


}
