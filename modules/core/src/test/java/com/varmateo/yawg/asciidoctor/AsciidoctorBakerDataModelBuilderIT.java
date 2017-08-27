/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Path;

import org.asciidoctor.Asciidoctor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.asciidoctor.AsciidoctorBakerDataModelBuilder;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateDataModel;


/**
 *
 */
public final class AsciidoctorBakerDataModelBuilderIT
 {


    private AsciidoctorBakerDataModelBuilder _modelBuilder = null;


    /**
     *
     */
    @Before
    public void setUp() {

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        _modelBuilder = new AsciidoctorBakerDataModelBuilder(asciidoctor);
    }


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithTitle.adoc");

        assertThat(model.getTitle()).isEqualTo("Document with Title");
        assertThat(model.getBody()).contains(
                "The body of the document with a title.");
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithoutTitle.adoc");

        assertThat(model.getTitle()).isEqualTo("DocumentWithoutTitle");
        assertThat(model.getBody()).contains(
                "The body of the document without a title.");
    }


    /**
     *
     */
    @Test
    public void withAuthor00()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor00.adoc");
        Iterable<TemplateDataModel.Author> authors = model.getAuthors();

        assertThat(authors).isEmpty();
    }


    /**
     *
     */
    @Test
    public void withAuthor01()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor01.adoc");
        Iterable<TemplateDataModel.Author> authors = model.getAuthors();

        assertThat(authors)
                .extracting("name", "email")
                .containsExactly(
                        tuple("John Doe", "john.doe@example.com"));
    }


    /**
     *
     */
    @Test
    public void withAuthor02()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor02.adoc");
        Iterable<TemplateDataModel.Author> authors = model.getAuthors();

        assertThat(authors)
                .extracting("name", "email")
                .containsExactly(
                        tuple("John Doe", "john.doe@example.com"));
    }


    /**
     *
     */
    @Test
    public void withAuthor03()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor03.adoc");
        Iterable<TemplateDataModel.Author> authors = model.getAuthors();

        assertThat(authors)
                .extracting("name", "email")
                .containsExactly(
                        tuple("John Doe", "john.doe@example.com"),
                        tuple("Jane Doe", "jane.doe@example.com"));
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
                TestUtils.getPath(
                        AsciidoctorBakerDataModelBuilder.class,
                        relPath);
        TemplateDataModel model =
                _modelBuilder.build(
                        sourcePath,
                        sourcePath.getParent(),
                        sourcePath,
                        context);

        return model;
    }


}
