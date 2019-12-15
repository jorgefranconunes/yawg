/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.spi.TemplateContextBuilder;
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

        final Path templatesDir = Paths.get("this/directory/does/not/exist");

        assertThatThrownBy(() -> FreemarkerTemplateService.create(templatesDir))
                .isInstanceOf(YawgException.class);
    }


    /**
     *
     */
    @Test
    public void withTemplateExists() {

        final Path templatesDir = TestUtils.getPath(
                FreemarkerTemplateService.class,
                TEMPLATE_DIR_OK);
        final TemplateService service = FreemarkerTemplateService.create(templatesDir);
        final Optional<Template> template = service.prepareTemplate(TEMPLATE_NAME_OK);

        assertThat(template).isPresent();
    }


    /**
     *
     */
    @Test
    public void withTemplateMissing() {

        final Path templatesDir = TestUtils.getPath(
                FreemarkerTemplateService.class,
                TEMPLATE_DIR_OK);
        final TemplateService service = FreemarkerTemplateService.create(templatesDir);

        assertThatThrownBy(() -> service.prepareTemplate("NoSuchTemplate.ftlh"))
                .isInstanceOf(YawgException.class);
    }


    /**
     *
     */
    @Test
    public void processTemplateOk() {

        final Path templatesDir = TestUtils.getPath(
                FreemarkerTemplateService.class,
                TEMPLATE_DIR_OK);
        final TemplateService service = FreemarkerTemplateService.create(templatesDir);
        final Template template = service.prepareTemplate("template02.ftlh").get();

        final String title = "Simple title";
        final String body = "Hello, world!";
        final TemplateContext model = TemplateContextBuilder.create()
                .body(body)
                .pageUrl("MyPage.html")
                .rootRelativeUrl(".")
                .title(title)
                .bakeId("TestBakeId")
                .build();
        final StringWriter buffer = new StringWriter();

        template.process(model, buffer);

        final String actualBakedContents = buffer.toString();
        final String expectedBakedContents = "Demo02: " + body + "\n";

        assertThat(actualBakedContents).isEqualTo(expectedBakedContents);
    }


    /**
     *
     */
    @Test
    public void givenInvalidTemplate_whenProcess_thenResultIsFailure() {

        // GIVEN
        final Path templatesDir = TestUtils.getPath(
                FreemarkerTemplateService.class,
                TEMPLATE_DIR_OK);
        final TemplateService service = FreemarkerTemplateService.create(templatesDir);
        final Template template = service.prepareTemplate("template03.ftlh").get();

        // WHEN
        final String title = "Simple title";
        final String body = "Hello, world!";
        final TemplateContext context = TemplateContextBuilder.create()
                .body(body)
                .pageUrl("MyPage.html")
                .rootRelativeUrl(".")
                .title(title)
                .bakeId("TestBakeId")
                .build();
        final StringWriter buffer = new StringWriter();
        final Result<Void> result = template.process(context, buffer);

        // THEN
        assertThat(result.isSuccess())
                .isFalse();
        assertThat(result.failureCause())
                .isInstanceOf(FreemarkerTemplateServiceException.class);
    }


}
