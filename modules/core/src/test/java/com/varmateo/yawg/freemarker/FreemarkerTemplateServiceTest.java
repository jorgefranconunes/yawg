/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import freemarker.template.TemplateException;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.freemarker.FreemarkerTemplateService;


/**
 *
 */
public final class FreemarkerTemplateServiceTest
 {


    private static final String TEMPLATE_NAME_OK = "template01.ftlh";
    private static final String TEMPLATE_DIR_OK = "templates";


    /**
     *
     */
    @Test
    public void wrongTemplatesDir() {

        Path templatesDir = Paths.get("this/directory/does/not/exist");
        TemplateService service =
                new FreemarkerTemplateService(templatesDir);

        assertThatThrownBy(() -> service.getTemplate(TEMPLATE_NAME_OK))
                .isInstanceOf(YawgException.class);
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
        TemplateService service =
                new FreemarkerTemplateService(templatesDir);
        Optional<Template> template = service.getTemplate(TEMPLATE_NAME_OK);

        assertThat(template).isPresent();
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
        TemplateService service =
                new FreemarkerTemplateService(templatesDir);

        assertThatThrownBy(() -> service.getTemplate("NoSuchTemplate.ftlh"))
                .isInstanceOf(YawgException.class);
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
        TemplateService service =
                new FreemarkerTemplateService(templatesDir);
        Template template = service.getTemplate("template02.ftlh").get();

        String title = "Simple title";
        String body = "Hello, world!";
        TemplateDataModel model =
                TemplateDataModel.builder()
                .setBody(body)
                .setPageUrl("MyPage.html")
                .setRootRelativeUrl(".")
                .setTitle(title)
                .build();
        StringWriter buffer = new StringWriter();

        template.process(model, buffer);

        String actualBakedContents = buffer.toString();
        String expectedBakedContents = "Demo02: " + body + "\n";

        assertThat(actualBakedContents).isEqualTo(expectedBakedContents);
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
        TemplateService service =
                new FreemarkerTemplateService(templatesDir);
        Template template = service.getTemplate("template03.ftlh").get();

        String title = "Simple title";
        String body = "Hello, world!";
        TemplateDataModel model =
                TemplateDataModel.builder()
                .setBody(body)
                .setPageUrl("MyPage.html")
                .setRootRelativeUrl(".")
                .setTitle(title)
                .build();
        StringWriter buffer = new StringWriter();

        assertThatThrownBy(() -> template.process(model, buffer))
                .hasCauseInstanceOf(TemplateException.class);
    }


}
