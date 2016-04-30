/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Optional;

import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.YawgException;


/**
 * Provider of layout templates for baking.
 */
public interface PageTemplateService {


    /**
     * Fetches the default template.
     *
     * @return A filled <code>Optional</code> if a default page
     * template is available. An empty one otherwise.
     *
     * @throws YawgException If for whatever reason it was not
     * possible to create the default page template.
     */
    Optional<PageTemplate> getDefaultTemplate()
            throws YawgException;


    /**
     * Fetches the template with the given name.
     *
     * <p>The meaning and structure of the template name depdend on
     * the concrete template framework being used.</p>
     *
     * <p>Do not assume that the same object will be returned for two
     * calls with the same name. You can only assume the template
     * objects returned for the same name are functionaly
     * equivalent.</p>
     *
     * @param name The name of the template to return.
     *
     * @throws YawgException If for whatever reason it was not
     * possible to create the page template.
     */
    PageTemplate getTemplate(String name)
            throws YawgException;
}
