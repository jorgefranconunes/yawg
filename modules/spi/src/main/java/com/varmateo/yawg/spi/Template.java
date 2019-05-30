/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.io.Writer;

import com.varmateo.yawg.api.YawgException;


/**
 * Layout template for a baked page.
 */
public interface Template {


    /**
     * Generates the final document from this template.
     *
     * @param dataModel Data that can be used by the template.
     *
     * @param output The target where the final document is to be
     * written to.
     *
     * @throws YawgException Thrown if there were any problems
     * processing the template or writing into the given writer.
     */
    void process(
            TemplateContext dataModel,
            Writer output)
            throws YawgException;
}
