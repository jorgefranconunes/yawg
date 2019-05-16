/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import com.varmateo.testutils.TestUtils;
import com.varmateo.yawg.html.HtmlBakerDataModelBuilder;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.TemplateDataModel;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


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

        final TemplateDataModel model = buildModel("DocumentWithTitle.html");

        assertThat(model.title())
                .isEqualTo("Document with Title");
        assertThat(model.body())
                .isEqualTo("<p>The body of the document with a title.</p>");
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        final TemplateDataModel model = buildModel("DocumentWithoutTitle.html");

        assertThat(model.title())
                .isEqualTo("DocumentWithoutTitle");
        assertThat(model.body())
                .isEqualTo("<p>The body of the document without a title.</p>");
    }


    /**
     *
     */
    private TemplateDataModel buildModel(final String relPath)
            throws IOException {

        final String dirUrl = ".";
        final String rootRelativeUrl = ".";
        final PageContext context = PageContextBuilder.create()
                .dirUrl(dirUrl)
                .rootRelativeUrl(rootRelativeUrl)
                .build();
        final Path sourcePath = TestUtils.getPath(HtmlBakerDataModelBuilder.class, relPath);

        return _modelBuilder.build(sourcePath, sourcePath, context);
    }


}
