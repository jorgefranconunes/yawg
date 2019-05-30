/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Path;

import org.asciidoctor.Asciidoctor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.varmateo.testutils.LogStartAndEndRule;
import com.varmateo.testutils.TestUtils;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.TemplateContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


/**
 *
 */
public final class AsciidoctorTemplateContextIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();

    private Asciidoctor _asciidoctor = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _asciidoctor = Asciidoctor.Factory.create();
    }


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        final TemplateContext model = buildTemplateContext("DocumentWithTitle.adoc");

        assertThat(model.title())
                .isEqualTo("Document with Title");
        assertThat(model.body())
                .contains("The body of the document with a title.");
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        final TemplateContext model = buildTemplateContext("DocumentWithoutTitle.adoc");

        assertThat(model.title())
                .isEqualTo("DocumentWithoutTitle");
        assertThat(model.body())
                .contains("The body of the document without a title.");
    }


    /**
     *
     */
    @Test
    public void withAuthor00()
            throws IOException {

        final TemplateContext model = buildTemplateContext("DocumentWithAuthor00.adoc");
        final Iterable<TemplateContext.Author> authors = model.authors();

        assertThat(authors)
                .isEmpty();
    }


    /**
     *
     */
    @Test
    public void withAuthor01()
            throws IOException {

        final TemplateContext model = buildTemplateContext("DocumentWithAuthor01.adoc");
        final Iterable<TemplateContext.Author> authors = model.authors();

        assertThat(authors)
                .extracting(author -> tuple(author.name(), author.email()))
                .containsExactly(
                        tuple("John Doe", "john.doe@example.com"));
    }


    /**
     *
     */
    @Test
    public void withAuthor02()
            throws IOException {

        final TemplateContext model = buildTemplateContext("DocumentWithAuthor02.adoc");
        final Iterable<TemplateContext.Author> authors = model.authors();

        assertThat(authors)
                .extracting(author -> tuple(author.name(), author.email()))
                .containsExactly(
                        tuple("John Doe", "john.doe@example.com"));
    }


    /**
     *
     */
    @Test
    public void withAuthor03()
            throws IOException {

        final TemplateContext model = buildTemplateContext("DocumentWithAuthor03.adoc");
        final Iterable<TemplateContext.Author> authors = model.authors();

        assertThat(authors)
                .extracting(author -> tuple(author.name(), author.email()))
                .containsExactly(
                        tuple("John Doe", "john.doe@example.com"),
                        tuple("Jane Doe", "jane.doe@example.com"));
    }


    /**
     *
     */
    private TemplateContext buildTemplateContext(final String relPath)
            throws IOException {

        final String dirUrl = ".";
        final String rootRelativeUrl = ".";
        final PageContext context = PageContextBuilder.create()
                .dirUrl(dirUrl)
                .rootRelativeUrl(rootRelativeUrl)
                .bakeId("TestBakeId")
                .build();
        final Path sourcePath = TestUtils.getPath(
                AsciidoctorTemplateContext.class,
                relPath);

        return AsciidoctorTemplateContext.create(
                _asciidoctor,
                sourcePath,
                sourcePath.getParent(),
                sourcePath,
                context);
    }


}
