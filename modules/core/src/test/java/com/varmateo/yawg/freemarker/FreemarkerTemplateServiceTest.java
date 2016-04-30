/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.freemarker.FreemarkerTemplateService;


/**
 *
 */
public final class FreemarkerTemplateServiceTest
        extends Object {


    /**
     *
     */
    @Test
    public void noTemplatesDir() {

        Optional<Path> templatesDir = Optional.empty();
        PageTemplateService service =
                new FreemarkerTemplateService(templatesDir);
        Optional<PageTemplate> template = service.getDefaultTemplate();

        assertFalse(template.isPresent());
    }


    /**
     *
     */
    @Test
    public void wrongTemplatesDir() {

        Path templatesDir = Paths.get("this/directory/does/not/exist");
        PageTemplateService service =
                new FreemarkerTemplateService(Optional.of(templatesDir));

        TestUtils.assertThrows(
                YawgException.class,
                () -> service.getDefaultTemplate());
    }


    /**
     *
     */
    @Test
    public void emptyTemplatesDir() {

        Path templatesDir =
                TestUtils.getPath(
                        FreemarkerTemplateServiceTest.class,
                        "emptyDir");
        PageTemplateService service =
                new FreemarkerTemplateService(Optional.of(templatesDir));

        TestUtils.assertThrows(
                YawgException.class,
                () -> service.getDefaultTemplate());
    }


    /**
     *
     */
    @Test
    public void withDefaultTemplate() {

        Path templatesDir =
                TestUtils.getPath(
                        FreemarkerTemplateServiceTest.class,
                        "okDir");
        PageTemplateService service =
                new FreemarkerTemplateService(Optional.of(templatesDir));
        Optional<PageTemplate> template = service.getDefaultTemplate();

        assertTrue(template.isPresent());
    }


}