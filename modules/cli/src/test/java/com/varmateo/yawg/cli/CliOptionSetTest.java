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

import com.varmateo.yawg.cli.CliOption;
import com.varmateo.yawg.cli.CliOptionSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 *
 */
public final class CliOptionSetTest {


    private static final String OPT_WITH_ARG_NAME = "option-with-arg";
    private static final String OPT_WITH_ARG_OPT = "--" + OPT_WITH_ARG_NAME;
    private static final CliOption OPT_WITH_ARG = CliOption.builder()
            .longName(OPT_WITH_ARG_NAME)
            .description("This is option has one argument")
            .argName("ARG")
            .build();

    private static final String OPT_NO_ARG_NAME = "option-with-no-arg";
    private static final String OPT_NO_ARG_OPT = "--" + OPT_NO_ARG_NAME;
    private static final CliOption OPT_NO_ARG = CliOption.builder()
            .longName(OPT_NO_ARG_NAME)
            .description("This is option has one argument")
            .build();


    @Test
    public void parseNoArgsNoOptions()
        throws CliException {

        Set<CliOption> options = HashSet.of();
        String[] args = {};
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);

        assertThat(cliOptions.supportedOptions()).isEmpty();
    }


    @Test
    public void parseArgsNoOptions() {

        Set<CliOption> options = HashSet.of();
        String[] args = { "--something" };

        assertThatThrownBy(() -> CliOptionSet.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void parseMissingOptionArg() {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT };

        assertThatThrownBy(() -> CliOptionSet.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void parseTooMany() {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something", "else" };

        assertThatThrownBy(() -> CliOptionSet.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void getStringMandatoryOk()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something" };
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG);

        assertThat(actualValue).isEqualTo("something");
    }


    @Test
    public void getStringMandatoryMissing()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = {};
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);

        assertThatThrownBy(() -> cliOptions.get(OPT_WITH_ARG))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void getDefaultWithValue()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something" };
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertThat(actualValue).isEqualTo("something");
    }


    @Test
    public void getDefaultWithoutValue()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = {};
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertThat(actualValue).isEqualTo("whatever");
    }


    @Test
    public void getPath()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "this/is/a/path" };
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);
        Path actualPath = cliOptions.getPath(OPT_WITH_ARG);
        Path expectedPath = Paths.get("this", "is", "a", "path");

        assertThat(actualPath).isEqualTo(expectedPath);
    }


    @Test
    public void isTrueWithArg()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "true" };
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_WITH_ARG)).isTrue();
    }


    @Test
    public void isFalseWithArg()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "false" };
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_WITH_ARG)).isFalse();
    }


    @Test
    public void isTrueNoArg()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_NO_ARG);
        String[] args = { OPT_NO_ARG_OPT };
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_NO_ARG)).isTrue();
    }


    @Test
    public void isFalseNoArg()
        throws CliException {

        Set<CliOption> options = HashSet.of(OPT_NO_ARG);
        String[] args = {};
        CliOptionSet cliOptions = CliOptionSet.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_NO_ARG)).isFalse();
    }


}
