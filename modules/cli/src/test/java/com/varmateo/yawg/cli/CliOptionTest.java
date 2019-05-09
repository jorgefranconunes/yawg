/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.varmateo.yawg.cli.CliOption;


/**
 *
 */
public final class CliOptionTest
 {


    /**
     *
     */
    @Test
    public void withoutArg() {

        final CliOption option = CliOption.builder()
                .shortName("demo")
                .build();

        assertThat(option.shortName()).isEqualTo("demo");
        assertThat(option.isWithArg()).isFalse();
    }


    /**
     *
     */
    @Test
    public void withArg() {

        final CliOption option = CliOption.builder()
                .shortName("demo")
                .argName("something")
                .build();

        assertThat(option.shortName()).isEqualTo("demo");
        assertThat(option.isWithArg()).isTrue();
        assertThat(option.argName()).isEqualTo("something");
    }


    /**
     *
     */
    @Test
    public void shortName() {

        final CliOption option = CliOption.builder()
                .shortName("s")
                .build();

        assertThat(option.shortName()).isEqualTo("s");
        assertThat(option.longName()).isNull();
        assertThat(option.name()).isEqualTo("s");
        assertThat(option.literal()).isEqualTo("-s");
    }


    /**
     *
     */
    @Test
    public void longName() {

        final CliOption option = CliOption.builder()
                .shortName("s")
                .longName("something")
                .build();

        assertThat(option.shortName()).isEqualTo("s");
        assertThat(option.longName()).isEqualTo("something");
        assertThat(option.name()).isEqualTo("something");
        assertThat(option.literal()).isEqualTo("--something");
    }


}
