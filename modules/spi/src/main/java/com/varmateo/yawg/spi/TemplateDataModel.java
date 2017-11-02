/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
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
    Iterable<Author> getAuthors();


    /**
     * The raw HTML contents of the baked document. This is actually
     * an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    String getBody();


    /**
     * The URL of the page being baked relative to the root directory
     * of the site.
     */
    String getPageUrl();


    /**
     * Set of variables available to the page template. These
     * variables are immutable.
     */
    PageVars getPageVars();


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
    //String getVersion();


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
        public String getName() {

            return _name;
        }


        /**
         * @return Author's email. It might be null.
         */
        public String getEmail() {

            return _email;
        }


    }


}
