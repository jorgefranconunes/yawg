/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Optional;

import com.varmateo.yawg.Template;
import com.varmateo.yawg.YawgException;


/**
 * Provider of layout templates for baking.
 */
public interface TemplateService {


    /**
     * Fetches the template with the given name.
     *
     * <p>The meaning and structure of the template name depend on the
     * concrete template engine being used.</p>
     *
     * <p>Do not assume that the same object will be returned for two
     * calls with the same name. You can only assume the template
     * objects returned for the same name are functionaly
     * equivalent.</p>
     *
     * @param name The name of the template to return.
     *
     * @return The template associated with the given name. If this
     * service does not support the template format then an empty
     * <code>Optional</code> will be returned.
     *
     * @throws YawgException If for whatever reason it was not
     * possible to create the page template. For instance, if the
     * template with the given name does not exist.
     */
    Optional<Template> getTemplate(String name)
            throws YawgException;
}
