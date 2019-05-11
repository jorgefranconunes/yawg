/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import com.varmateo.yawg.spi.PageVars;


/**
 * Data available to a template during processing.
 */
public interface TemplateDataModel {


    /**
     * The authors of the document. It may be an empty list.
     */
    Iterable<Author> authors();


    /**
     * The raw HTML contents of the baked document. This is actually
     * an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    String body();


    /**
     * The URL of the page being baked relative to the root directory
     * of the site.
     */
    String pageUrl();


    /**
     * Set of variables available to the page template. These
     * variables are immutable.
     */
    PageVars pageVars();


    /**
     * The URL of the root directory of the site being baked relative
     * to the document about to be baked.
     */
    String rootRelativeUrl();


    /**
     * The title of the document, as extracted from its source.
     */
    String title();


    /**
     * Document author data.
     */
    public static final class Author {


        private final String _name;
        private final String _email;


        /**
         *
         */
        private Author(
                final String name,
                final String email) {

            _name = name;
            _email = email;
        }


        /**
         *
         */
        public static Author create(
                final String name,
                final String email) {

            return new Author(name, email);
        }


        /**
         * @return Author's name. It is never null.
         */
        public String name() {

            return _name;
        }


        /**
         * @return Author's email. It might be null.
         */
        public String email() {

            return _email;
        }


    }


}
