/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
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

        CliOption option =
            CliOption.builder()
            .setShortName("demo")
            .build();

        assertThat(option.getShortName()).isEqualTo("demo");
        assertThat(option.isWithArg()).isFalse();
    }


    /**
     *
     */
    @Test
    public void withArg() {

        CliOption option =
            CliOption.builder()
            .setShortName("demo")
            .setArgName("something")
            .build();

        assertThat(option.getShortName()).isEqualTo("demo");
        assertThat(option.isWithArg()).isTrue();
        assertThat(option.getArgName()).isEqualTo("something");
    }


    /**
     *
     */
    @Test
    public void shortName() {

        CliOption option =
            CliOption.builder()
            .setShortName("s")
            .build();

        assertThat(option.getShortName()).isEqualTo("s");
        assertThat(option.getLongName()).isNull();
        assertThat(option.getName()).isEqualTo("s");
        assertThat(option.getLiteral()).isEqualTo("-s");
    }


    /**
     *
     */
    @Test
    public void longName() {

        CliOption option =
            CliOption.builder()
            .setShortName("s")
            .setLongName("something")
            .build();

        assertThat(option.getShortName()).isEqualTo("s");
        assertThat(option.getLongName()).isEqualTo("something");
        assertThat(option.getName()).isEqualTo("something");
        assertThat(option.getLiteral()).isEqualTo("--something");
    }


}
