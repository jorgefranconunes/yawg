/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.Template;


/**
 * Information to be used by a baker when baking a file.
 */
public interface PageContext {


    /**
     * The URL of the directory containing the current page relative
     * to the top of the document tree.
     */
    String getDirUrl();


    /**
     * Template to be used when generating the target page.
     */
    Optional<Template> getTemplateFor(Path path);


    /**
     * Additional variables made available to the template.
     */
    PageVars getPageVars();


    /**
     * The URL of the top of the baked site relative to the current
     * page. Useful for refering to resources at the top of the
     * document tree.
     */
    String getRootRelativeUrl();

}
