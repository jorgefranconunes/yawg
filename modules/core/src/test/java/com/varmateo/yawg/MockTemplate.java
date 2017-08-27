/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Writer;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.util.Exceptions;


/**
 *
 */
public final class MockTemplate
        implements Template {


    /**
     *
     */
    @Override
    public void process(
            TemplateDataModel model,
            Writer output)
            throws YawgException {

        try {
            output.write(model.getBody());
        } catch ( IOException e ) {
            Exceptions.raise(
                    e,
                    "Failed to write page - {0} - {1}",
                    e.getClass().getName(),
                    e.getMessage());
        }
    }


}


