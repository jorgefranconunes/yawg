/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Objects;
import java.util.UUID;

import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.YawgInfo;


/**
 * Data available to a template during processing.
 */
public interface TemplateDataModel {


    /**
     * Randomly generated unique bake identifier. Each bake will have
     * a different identifier.
     */
    String getBakeId();


    /**
     * The raw HTML contents of the baked document. This is actually
     * an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    String getBody();


    /**
     *
     */
    String getPageUrl();


    /**
     * Set of variables available to the page template. These
     * variables are immutable.
     */
    PageVars getPageVars();


    /**
     * A fixed string with the Yawg product name.
     */
    String getProductName();


    /**
     * The URL of the root directory of the site being baked relative
     * to the document about to be baked.
     */
    String getRootRelativeUrl();


    /**
     * The title of the document, as extracted from its source.
     */
    String getTitle();


    /**
     * The version of the Yawg software being used.
     */
    String getVersion();


}
