/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.api;

import java.io.Writer;

import com.varmateo.yawg.api.TemplateDataModel;
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
     * @param output The target where the final document is written to.
     *
     * @throws YawgException Thrown if there were any problems
     * processing the template or writing into the given writer.
     */
    void process(
            TemplateDataModel dataModel,
            Writer output)
            throws YawgException;
}
