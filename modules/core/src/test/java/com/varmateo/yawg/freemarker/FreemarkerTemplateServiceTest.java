/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.nio.file.Path;
import java.nio.file.Paths;

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


    private static final String TEMPLATE_NAME_OK = "default.ftlh";


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
    public void withTemplate() {

        Path templatesDir =
                TestUtils.getPath(
                        FreemarkerTemplateService.class,
                        "okDir");
        PageTemplateService service =
                new FreemarkerTemplateService(templatesDir);
        PageTemplate template = service.getTemplate(TEMPLATE_NAME_OK);

        assertNotNull(template);
    }


}