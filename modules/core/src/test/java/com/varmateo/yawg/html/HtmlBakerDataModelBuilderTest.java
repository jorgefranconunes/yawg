/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateDataModel;

import com.varmateo.yawg.core.PageContextBuilder;
import com.varmateo.yawg.html.HtmlBakerDataModelBuilder;


/**
 *
 */
public final class HtmlBakerDataModelBuilderTest
        extends Object {


    private HtmlBakerDataModelBuilder _modelBuilder = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _modelBuilder = new HtmlBakerDataModelBuilder();
    }


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithTitle.html");

        assertEquals("Document with Title", model.title);
        assertEquals(
                "<p>The body of the document with a title.</p>",
                model.body);
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithoutTitle.html");

        assertEquals("DocumentWithoutTitle", model.title);
        assertEquals(
                "<p>The body of the document without a title.</p>",
                model.body);
    }


    /**
     *
     */
    private TemplateDataModel buildModel(final String relPath)
            throws IOException {

        String dirUrl = ".";
        String rootRelativeUrl = ".";
        PageContext context =
                new PageContextBuilder()
                .setDirUrl(dirUrl)
                .setRootRelativeUrl(rootRelativeUrl)
                .build();
        Path sourcePath =
                TestUtils.getPath(HtmlBakerDataModelBuilder.class, relPath);
        TemplateDataModel model =
                _modelBuilder.build(sourcePath, sourcePath, context);

        return model;
    }


}
