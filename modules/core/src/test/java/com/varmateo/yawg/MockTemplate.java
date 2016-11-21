/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.io.Writer;

import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.util.Exceptions;


/**
 *
 */
public final class MockTemplate
        implements Template {


    private TemplateDataModel _model = null;


    /**
     *
     */
    public MockTemplate() {
        // Nothing to do.
    }


    /**
     *
     */
    @Override
    public void process(
            TemplateDataModel model,
            Writer output)
            throws YawgException {

        _model = model;

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


