/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import org.junit.Test;

import freemarker.template.TemplateException;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;


/**
 *
 */
public final class FreemarkerTemplateServiceTest
        extends Object {


    private static final String TEMPLATE_NAME_OK = "template01.fthl";
    private static final String TEMPLATE_DIR_OK = "templates";


    /**
     *
     */
    @Test
    public void wrongTemplatesDir() {

        Path templatesDir = Paths.get("this/directory/does/not/exist");
        PageTemplateService service =
                new FreemarkerTemplateService(templatesDir);

        TestUtils.assertThrows(
                YawgException.class,
                () -> service.getTemplate(TEMPLATE_NAME_OK));
    }


    /**
     *
     */
    @Test
    public void withTemplateExists() {

        Path templatesDir =
                TestUtils.getPath(
                        FreemarkerTemplateService.class,
                        TEMPLATE_DIR_OK);
        PageTemplateService service =
                new FreemarkerTemplateService(templatesDir);
        PageTemplate template = service.getTemplate(TEMPLATE_NAME_OK);

        assertNotNull(template);
    }


    /**
     *
     */
    @Test
    public void withTemplateMissing() {

        Path templatesDir =
                TestUtils.getPath(
                        FreemarkerTemplateService.class,
                        TEMPLATE_DIR_OK);
        PageTemplateService service =
                new FreemarkerTemplateService(templatesDir);

        TestUtils.assertThrows(
                YawgException.class,
                () -> service.getTemplate("NoSuchTemplate.fthl"));
    }


    /**
     *
     */
    @Test
    public void processTemplateOk() {

        Path templatesDir =
                TestUtils.getPath(
                        FreemarkerTemplateService.class,
                        TEMPLATE_DIR_OK);
        PageTemplateService service =
                new FreemarkerTemplateService(templatesDir);
        PageTemplate template = service.getTemplate("template02.fthl");

        String title = "Simple title";
        String body = "Hello, world!";
        PageTemplateDataModel model =
                new PageTemplateDataModel.Builder()
                .setBody(body)
                .setTitle(title)
                .setRootRelativeUrl(".")
                .build();
        StringWriter buffer = new StringWriter();

        template.process(model, buffer);

        String actualBakedContents = buffer.toString();
        String expectedBakedContents = "Demo02: " + body + "\n";

        assertEquals(expectedBakedContents, actualBakedContents);
    }


    /**
     *
     */
    @Test
    public void processTemplateNotOk() {

        Path templatesDir =
                TestUtils.getPath(
                        FreemarkerTemplateService.class,
                        TEMPLATE_DIR_OK);
        PageTemplateService service =
                new FreemarkerTemplateService(templatesDir);
        PageTemplate template = service.getTemplate("template03.fthl");

        String title = "Simple title";
        String body = "Hello, world!";
        PageTemplateDataModel model =
                new PageTemplateDataModel.Builder()
                .setBody(body)
                .setTitle(title)
                .setRootRelativeUrl(".")
                .build();
        StringWriter buffer = new StringWriter();

        Exception error =
                TestUtils.assertThrows(
                        YawgException.class,
                        () -> template.process(model, buffer));
        Throwable causeError = error.getCause();

        assertTrue(causeError instanceof TemplateException);
    }


}