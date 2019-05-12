/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 *
 */
public final class CliParameterSetTest {


    private static final String OPT_WITH_ARG_NAME = "option-with-arg";
    private static final String OPT_WITH_ARG_OPT = "--" + OPT_WITH_ARG_NAME;
    private static final CliParameter OPT_WITH_ARG = CliParameter.builder()
            .longName(OPT_WITH_ARG_NAME)
            .description("This is option has one argument")
            .argName("ARG")
            .build();

    private static final String OPT_NO_ARG_NAME = "option-with-no-arg";
    private static final String OPT_NO_ARG_OPT = "--" + OPT_NO_ARG_NAME;
    private static final CliParameter OPT_NO_ARG = CliParameter.builder()
            .longName(OPT_NO_ARG_NAME)
            .description("This is option has one argument")
            .build();


    @Test
    public void parseNoArgsNoOptions()
        throws CliException {

        final Set<CliParameter> options = HashSet.of();
        final String[] args = {};
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);

        assertThat(cliOptions.supportedOptions()).isEmpty();
    }


    @Test
    public void parseArgsNoOptions() {

        final Set<CliParameter> options = HashSet.of();
        final String[] args = { "--something" };

        assertThatThrownBy(() -> CliParameterSet.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void parseMissingOptionArg() {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = { OPT_WITH_ARG_OPT };

        assertThatThrownBy(() -> CliParameterSet.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void parseTooMany() {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = { OPT_WITH_ARG_OPT, "something", "else" };

        assertThatThrownBy(() -> CliParameterSet.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void getStringMandatoryOk()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = { OPT_WITH_ARG_OPT, "something" };
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);
        final String actualValue = cliOptions.get(OPT_WITH_ARG);

        assertThat(actualValue).isEqualTo("something");
    }


    @Test
    public void getStringMandatoryMissing()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = {};
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);

        assertThatThrownBy(() -> cliOptions.get(OPT_WITH_ARG))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void getDefaultWithValue()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = { OPT_WITH_ARG_OPT, "something" };
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);
        final String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertThat(actualValue).isEqualTo("something");
    }


    @Test
    public void getDefaultWithoutValue()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = {};
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);
        final String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertThat(actualValue).isEqualTo("whatever");
    }


    @Test
    public void getPath()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = { OPT_WITH_ARG_OPT, "this/is/a/path" };
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);
        final Path actualPath = cliOptions.getPath(OPT_WITH_ARG);
        final Path expectedPath = Paths.get("this", "is", "a", "path");

        assertThat(actualPath).isEqualTo(expectedPath);
    }


    @Test
    public void isTrueWithArg()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = { OPT_WITH_ARG_OPT, "true" };
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_WITH_ARG)).isTrue();
    }


    @Test
    public void isFalseWithArg()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_WITH_ARG);
        final String[] args = { OPT_WITH_ARG_OPT, "false" };
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_WITH_ARG)).isFalse();
    }


    @Test
    public void isTrueNoArg()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_NO_ARG);
        final String[] args = { OPT_NO_ARG_OPT };
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_NO_ARG)).isTrue();
    }


    @Test
    public void isFalseNoArg()
        throws CliException {

        final Set<CliParameter> options = HashSet.of(OPT_NO_ARG);
        final String[] args = {};
        final CliParameterSet cliOptions = CliParameterSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_NO_ARG)).isFalse();
    }


}
