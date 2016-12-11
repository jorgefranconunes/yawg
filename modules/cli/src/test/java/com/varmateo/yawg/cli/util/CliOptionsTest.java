/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.cli.util.CliOption;
import com.varmateo.yawg.cli.util.CliOptions;


/**
 *
 */
public final class CliOptionsTest
 {


    private static final CliOption OPT1 =
        CliOption.builder()
        .setLongName("option1")
        .setDescription("This is option one")
        .setShortName("o1")
        .build();

    private static final String OPT_WITH_ARG_NAME = "option-with-arg";
    private static final String OPT_WITH_ARG_OPT = "--" + OPT_WITH_ARG_NAME;
    private static final CliOption OPT_WITH_ARG =
        CliOption.builder()
        .setLongName(OPT_WITH_ARG_NAME)
        .setDescription("This is option has one argument")
        .setArgName("ARG")
        .build();

    private static final String OPT_NO_ARG_NAME = "option-with-no-arg";
    private static final String OPT_NO_ARG_OPT = "--" + OPT_WITH_ARG_NAME;
    private static final CliOption OPT_NO_ARG =
        CliOption.builder()
        .setLongName(OPT_WITH_ARG_NAME)
        .setDescription("This is option has one argument")
        .build();


    @Test
    public void parseNoArgsNoOptions()
        throws CliException {

        Collection<CliOption> options = Collections.emptyList();
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertThat(cliOptions.supportedOptions()).isEmpty();
    }


    @Test
    public void parseArgsNoOptions() {

        Collection<CliOption> options = Collections.emptyList();
        String[] args = { "--something" };

        assertThatThrownBy(() -> CliOptions.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void parseMissingOptionArg() {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT };

        assertThatThrownBy(() -> CliOptions.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void parseTooMany() {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something", "else" };

        assertThatThrownBy(() -> CliOptions.parse(options, args))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void getStringMandatoryOk()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something" };
        CliOptions cliOptions = CliOptions.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG);

        assertThat(actualValue).isEqualTo("something");
    }


    @Test
    public void getStringMandatoryMissing()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertThatThrownBy(() -> cliOptions.get(OPT_WITH_ARG))
                .isInstanceOf(CliException.class);
    }


    @Test
    public void getDefaultWithValue()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "something" };
        CliOptions cliOptions = CliOptions.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertThat(actualValue).isEqualTo("something");
    }


    @Test
    public void getDefaultWithoutValue()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);
        String actualValue = cliOptions.get(OPT_WITH_ARG, "whatever");

        assertThat(actualValue).isEqualTo("whatever");
    }


    @Test
    public void getPath()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "this/is/a/path" };
        CliOptions cliOptions = CliOptions.parse(options, args);
        Path actualPath = cliOptions.getPath(OPT_WITH_ARG);
        Path expectedPath = Paths.get("this", "is", "a", "path");

        assertThat(actualPath).isEqualTo(expectedPath);
    }


    @Test
    public void isTrueWithArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "true" };
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_WITH_ARG)).isTrue();
    }


    @Test
    public void isFalseWithArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_WITH_ARG);
        String[] args = { OPT_WITH_ARG_OPT, "false" };
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_WITH_ARG)).isFalse();
    }


    @Test
    public void isTrueNoArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_NO_ARG);
        String[] args = { OPT_NO_ARG_OPT };
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_NO_ARG)).isTrue();
    }


    @Test
    public void isFalseNoArg()
        throws CliException {

        Collection<CliOption> options = Arrays.asList(OPT_NO_ARG);
        String[] args = {};
        CliOptions cliOptions = CliOptions.parse(options, args);

        assertThat(cliOptions.isTrue(OPT_NO_ARG)).isFalse();
    }


}
