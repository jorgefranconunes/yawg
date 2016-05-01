/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.html.HtmlBakerDataModelBuilder;


/**
 *
 */
public final class HtmlBakerDataModelBuilderTest
        extends Object {


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        Path sourcePath =
                TestUtils.getPath(
                        HtmlBakerDataModelBuilder.class,
                        "DocumentWithTitle.html");
        PageTemplateDataModel model =
                new HtmlBakerDataModelBuilder()
                .build(sourcePath);

        assertEquals("Document with Title", model.getTitle());
        assertEquals(
                "<p>The body of the document with a title.</p>",
                model.getBody());
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        Path sourcePath =
                TestUtils.getPath(
                        HtmlBakerDataModelBuilder.class,
                        "DocumentWithoutTitle.html");
        PageTemplateDataModel model =
                new HtmlBakerDataModelBuilder()
                .build(sourcePath);

        assertEquals("DocumentWithoutTitle", model.getTitle());
        assertEquals(
                "<p>The body of the document without a title.</p>",
                model.getBody());
    }


}
