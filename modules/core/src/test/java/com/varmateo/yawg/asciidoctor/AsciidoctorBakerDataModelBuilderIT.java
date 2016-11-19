/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.asciidoctor.Asciidoctor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateDataModel;

import com.varmateo.yawg.asciidoctor.AsciidoctorBakerDataModelBuilder;


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

        assertEquals("Document with Title", model.getTitle());
        assertTrue(model.getBody().contains(
                "The body of the document with a title."));
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithoutTitle.adoc");

        assertEquals("DocumentWithoutTitle", model.getTitle());
        assertTrue(model.getBody().contains(
                "The body of the document without a title."));
    }


    /**
     *
     */
    @Test
    public void withAuthor00()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor00.adoc");
        List<TemplateDataModel.Author> authors = model.getAuthors();

        assertEquals(0, authors.size());
    }


    /**
     *
     */
    @Test
    public void withAuthor01()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor01.adoc");
        List<TemplateDataModel.Author> authors = model.getAuthors();

        assertEquals(1, authors.size());
        assertEquals("John Doe", authors.get(0).getName());
        assertEquals("john.doe@example.com", authors.get(0).getEmail());
    }


    /**
     *
     */
    @Test
    public void withAuthor02()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor02.adoc");
        List<TemplateDataModel.Author> authors = model.getAuthors();

        assertEquals(1, authors.size());
        assertEquals("John Doe", authors.get(0).getName());
        assertEquals("john.doe@example.com", authors.get(0).getEmail());
    }


    /**
     *
     */
    @Test
    public void withAuthor03()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithAuthor03.adoc");
        List<TemplateDataModel.Author> authors = model.getAuthors();

        assertEquals(2, authors.size());
        assertEquals("John Doe", authors.get(0).getName());
        assertEquals("john.doe@example.com", authors.get(0).getEmail());
        assertEquals("Jane Doe", authors.get(1).getName());
        assertEquals("jane.doe@example.com", authors.get(1).getEmail());
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
