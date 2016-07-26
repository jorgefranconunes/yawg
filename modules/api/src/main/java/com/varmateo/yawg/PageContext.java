/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;
import java.util.Optional;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.Template;


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
    Optional<Template> getPageTemplate();


    /**
     * The URL of the top of the baked site relative to the current
     * page. Useful for refering to resources at the top of the
     * document tree.
     */
    String getRootRelativeUrl();


    /**
     * Additional variables made available to the template.
     */
    PageVars getPageVars();


}
