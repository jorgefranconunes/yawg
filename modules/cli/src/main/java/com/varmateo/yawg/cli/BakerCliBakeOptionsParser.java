/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.nio.file.Path;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;


/**
 *
 */
/* default */ final class BakerCliBakeOptionsParser {


    private BakerCliBakeOptionsParser() {
        // Nothing to do.
    }


    /**
     *
     */
    public static Try<BakerCliBakeOptions> parse(final String[] args) {

        return BakerCliParameters.parse(args)
                .flatMap(BakerCliBakeOptionsParser::parse);
    }


    /**
     *
     */
    public static Try<BakerCliBakeOptions> parse(final CliParameterSet cliParams) {

        return Try.of(() -> doParseParameters(cliParams));
    }


    private static BakerCliBakeOptions doParseParameters(final CliParameterSet cliParams)
            throws CliException {

        final Path sourceDir = cliParams.getPath(BakerCliParameters.SOURCE_DIR);
        final Path targetDir = cliParams.getPath(BakerCliParameters.TARGET_DIR);
        final Path assetsDir = cliParams.getPath(BakerCliParameters.ASSETS_DIR, null);
        final Path templatesDir = cliParams.getPath(BakerCliParameters.TEMPLATES_DIR, null);
        final Map<String, String> externalPageVars = buildExternalPageVars(cliParams);

        return BakerCliBakeOptions.builder()
                .sourceDir(sourceDir)
                .targetDir(targetDir)
                .assetsDir(Option.of(assetsDir))
                .templatesDir(Option.of(templatesDir))
                .externalPageVars(externalPageVars)
                .build();
    }


    private static Map<String, String> buildExternalPageVars(final CliParameterSet cliParams) {

        return cliParams
                .getAll(BakerCliParameters.PAGE_VAR)
                .map(BakerCliBakeOptionsParser::getVarNameAndValueFromOptionValue)
                .foldLeft(
                        HashMap.empty(),
                        (map, t) -> map.put(t._1, t._2));
    }


    private static Tuple2<String,String> getVarNameAndValueFromOptionValue(
            final String optionValue) {

        final String varName;
        final String varValue;
        final int indexOfEqSign = optionValue.indexOf('=');

        if ( indexOfEqSign < 0 ) {
            varName = optionValue;
            varValue = "";
        } else {
            varName = optionValue.substring(0, indexOfEqSign);
            varValue = optionValue.substring(indexOfEqSign+1);
        }

        return Tuple.of(varName, varValue);
    }

}
