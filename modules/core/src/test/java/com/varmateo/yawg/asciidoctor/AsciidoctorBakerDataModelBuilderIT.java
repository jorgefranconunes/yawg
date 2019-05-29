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
import com.varmateo.yawg.asciidoctor.AsciidoctorBakerDataModelBuilder;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.TemplateDataModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


/**
 *
 */
public final class AsciidoctorBakerDataModelBuilderIT {

    @Rule
    public final LogStartAndEndRule logRule = new LogStartAndEndRule();

    private AsciidoctorBakerDataModelBuilder _modelBuilder = null;


    /**
     *
     */
    @Before
    public void setUp() {

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        _modelBuilder = new AsciidoctorBakerDataModelBuilder(asciidoctor);
    }


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        final TemplateDataModel model = buildModel("DocumentWithTitle.adoc");

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

        final TemplateDataModel model = buildModel("DocumentWithoutTitle.adoc");

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

        final TemplateDataModel model = buildModel("DocumentWithAuthor00.adoc");
        final Iterable<TemplateDataModel.Author> authors = model.authors();

        assertThat(authors)
                .isEmpty();
    }


    /**
     *
     */
    @Test
    public void withAuthor01()
            throws IOException {

        final TemplateDataModel model = buildModel("DocumentWithAuthor01.adoc");
        final Iterable<TemplateDataModel.Author> authors = model.authors();

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

        final TemplateDataModel model = buildModel("DocumentWithAuthor02.adoc");
        final Iterable<TemplateDataModel.Author> authors = model.authors();

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

        final TemplateDataModel model = buildModel("DocumentWithAuthor03.adoc");
        final Iterable<TemplateDataModel.Author> authors = model.authors();

        assertThat(authors)
                .extracting(author -> tuple(author.name(), author.email()))
                .containsExactly(
                        tuple("John Doe", "john.doe@example.com"),
                        tuple("Jane Doe", "jane.doe@example.com"));
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
                .bakeId("TestBakeId")
                .build();
        final Path sourcePath = TestUtils.getPath(
                AsciidoctorBakerDataModelBuilder.class,
                relPath);

        return _modelBuilder.build(
                sourcePath,
                sourcePath.getParent(),
                sourcePath,
                context);
    }


}
