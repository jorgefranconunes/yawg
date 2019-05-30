/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;
import com.varmateo.yawg.html.HtmlTemplateContext;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.TemplateContext;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 */
public final class HtmlTemplateContextTest
 {


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        final TemplateContext model = buildModel("DocumentWithTitle.html");

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

        final TemplateContext model = buildModel("DocumentWithoutTitle.html");

        assertThat(model.title())
                .isEqualTo("DocumentWithoutTitle");
        assertThat(model.body())
                .isEqualTo("<p>The body of the document without a title.</p>");
    }


    /**
     *
     */
    private TemplateContext buildModel(final String relPath)
            throws IOException {

        final String dirUrl = ".";
        final String rootRelativeUrl = ".";
        final PageContext context = PageContextBuilder.create()
                .dirUrl(dirUrl)
                .rootRelativeUrl(rootRelativeUrl)
                .bakeId("TestBakeId")
                .build();
        final Path sourcePath = TestUtils.getPath(HtmlTemplateContext.class, relPath);

        return HtmlTemplateContext.create(sourcePath, sourcePath, context);
    }


}
