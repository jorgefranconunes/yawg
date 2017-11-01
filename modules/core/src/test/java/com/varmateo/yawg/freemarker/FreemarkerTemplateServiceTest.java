/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.spi.TemplateDataModelBuilder;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;
import com.varmateo.testutils.TestUtils;
import freemarker.template.TemplateException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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

        assertThatThrownBy(() -> FreemarkerTemplateService.build(templatesDir))
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
        TemplateService service = FreemarkerTemplateService.build(templatesDir);
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
        TemplateService service = FreemarkerTemplateService.build(templatesDir);

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
        TemplateService service = FreemarkerTemplateService.build(templatesDir);
        Template template = service.getTemplate("template02.ftlh").get();

        String title = "Simple title";
        String body = "Hello, world!";
        TemplateDataModel model =
                TemplateDataModelBuilder.create()
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

        Path templatesDir = TestUtils.getPath(
                FreemarkerTemplateService.class,
                TEMPLATE_DIR_OK);
        TemplateService service = FreemarkerTemplateService.build(templatesDir);
        Template template = service.getTemplate("template03.ftlh").get();

        String title = "Simple title";
        String body = "Hello, world!";
        TemplateDataModel model = TemplateDataModelBuilder.create()
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
