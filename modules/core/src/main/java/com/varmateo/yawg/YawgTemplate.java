/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.Writer;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.YawgTemplateDataModel;


/**
 *
 */
public interface YawgTemplate {


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
            YawgTemplateDataModel dataModel,
            Writer output)
            throws YawgException;
}
