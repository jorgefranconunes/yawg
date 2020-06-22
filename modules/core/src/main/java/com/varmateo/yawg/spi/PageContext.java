/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.nio.file.Path;
import java.util.Optional;


/**
 * Information to be used by a baker when baking a file.
 */
public interface PageContext {


    /**
     * The URL of the directory containing the current page relative
     * to the top of the document tree.
     */
    String dirUrl();


    /**
     * Template to be used when generating the target page.
     */
    Optional<Template> templateFor(Path path);


    /**
     * Additional variables made available to the template.
     */
    PageVars pageVars();


    /**
     * The URL of the top of the baked site relative to the current
     * page. Useful for refering to resources at the top of the
     * document tree.
     */
    String rootRelativeUrl();


    /**
     * Randomly generated unique bake identifier. Each bake will have
     * a different identifier.
     */
    String bakeId();

}
