/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.api.PageContext;
import com.varmateo.yawg.api.Template;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.asciidoctor.AsciidoctorBakerService;


/**
 *
 */
public final class AsciidoctorBakerServiceIT {


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
