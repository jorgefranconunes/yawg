/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.asciidoctor.AsciidoctorBaker;


/**
 *
 */
public final class AsciidoctorBakerIT
 {


    private AsciidoctorBaker _baker = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _baker = new AsciidoctorBaker();
    }


    /**
     *
     */
    @Test
    public void checkIsBakeable() {

        Path pathBakeable = Paths.get("SomethingBakeable.adoc");
        assertTrue(_baker.isBakeable(pathBakeable));

        Path pathNonBakeable = Paths.get("SomethingNotBakeable.txt");
        assertFalse(_baker.isBakeable(pathNonBakeable));
    }


}
