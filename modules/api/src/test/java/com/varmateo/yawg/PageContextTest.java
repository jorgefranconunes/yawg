/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Paths;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;


/**
 *
 */
public final class PageContextTest
 {


    /**
     *
     */
    @Test
    public void missingMandatoryAttrs() {

        TestUtils.assertThrows(
                NullPointerException.class,
                () -> PageContext.builder().build());
    }


    /**
     *
     */
    @Test
    public void mandatoryAttrs() {

        PageContext context =
                PageContext.builder()
                .setDirUrl("something")
                .setRootRelativeUrl("whatever")
                .build();

        assertEquals("something", context.getDirUrl());
        assertEquals("whatever", context.getRootRelativeUrl());
        assertFalse(context.getTemplateFor(Paths.get("xxx")).isPresent());
        assertTrue(context.getPageVars().asMap().isEmpty());
    }


}
