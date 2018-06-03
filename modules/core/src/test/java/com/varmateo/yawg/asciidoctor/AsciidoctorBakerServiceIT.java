/**************************************************************************
 *
 * Copyright (c) 2016-2018 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.yawg.asciidoctor.AsciidoctorBakerService;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 */
public final class AsciidoctorBakerServiceIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();

    private AsciidoctorBakerService _baker = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _baker = new AsciidoctorBakerService();
    }


    /**
     *
     */
    @Test
    public void checkIsBakeable() {

        Path pathBakeable = Paths.get("SomethingBakeable.adoc");
        assertThat(_baker.isBakeable(pathBakeable)).isTrue();

        Path pathNonBakeable = Paths.get("SomethingNotBakeable.txt");
        assertThat(_baker.isBakeable(pathNonBakeable)).isFalse();
    }


}
