/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.io.Writer;

import com.varmateo.yawg.api.Result;


/**
 * Layout template for a baked page.
 */
public interface Template {


    /**
     * Generates the final document from this template.
     *
     * @param context Input data for the document content generation
     * process.
     *
     * @param output Sink where the document content is to be written
     * to.
     *
     * @return A result signaling success, or failure. A failure could
     * be caused during the processing of the template, or writing
     * into the given {@code Writer}.
     */
    Result<Void> process(
            TemplateContext context,
            Writer output);
}
