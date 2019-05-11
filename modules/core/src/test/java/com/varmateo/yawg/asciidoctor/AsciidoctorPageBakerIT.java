/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.yawg.asciidoctor.AsciidoctorPageBaker;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 */
public final class AsciidoctorPageBakerIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();

    private AsciidoctorPageBaker _baker = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _baker = new AsciidoctorPageBaker();
    }


    /**
     *
     */
    @Test
    public void checkIsBakeable() {

        final Path pathBakeable = Paths.get("SomethingBakeable.adoc");
        assertThat(_baker.isBakeable(pathBakeable))
                .isTrue();

        final Path pathNonBakeable = Paths.get("SomethingNotBakeable.txt");
        assertThat(_baker.isBakeable(pathNonBakeable))
                .isFalse();
    }


}
