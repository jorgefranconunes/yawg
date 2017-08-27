/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.html.HtmlBakerDataModelBuilder;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateDataModel;


/**
 *
 */
public final class HtmlBakerDataModelBuilderTest
 {


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

        assertThat(model.getTitle()).isEqualTo("Document with Title");
        assertThat(model.getBody())
                .isEqualTo("<p>The body of the document with a title.</p>");
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithoutTitle.html");

        assertThat(model.getTitle()).isEqualTo("DocumentWithoutTitle");
        assertThat(model.getBody())
                .isEqualTo("<p>The body of the document without a title.</p>");
    }


    /**
     *
     */
    private TemplateDataModel buildModel(final String relPath)
            throws IOException {

        String dirUrl = ".";
        String rootRelativeUrl = ".";
        PageContext context =
                PageContext.builder()
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
