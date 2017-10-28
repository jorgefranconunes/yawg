/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.varmateo.yawg.api.YawgInfo;
import com.varmateo.yawg.spi.PageVars;


/**
 * Data available to a template during processing.
 */
public final class TemplateDataModel {


    private final List<Author> _authors;
    private final String _bakeId;
    private final String _body;
    private final String _pageUrl;
    private final PageVars _pageVars;
    private final String _productName;
    private final String _rootRelativeUrl;
    private final String _title;
    private final String _version;


    /**
     *
     */
    private TemplateDataModel(final Builder builder) {

        _authors =
                Collections.unmodifiableList(new ArrayList<>(builder._authors));
        _bakeId = UUID.randomUUID().toString();
        _body = Objects.requireNonNull(builder._body);
        _pageUrl = Objects.requireNonNull(builder._pageUrl);
        _pageVars =
                (builder._pageVars!=null) ? builder._pageVars: PageVars.empty();
        _productName = YawgInfo.PRODUCT_NAME;
        _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
        _title = Objects.requireNonNull(builder._title);
        _version = YawgInfo.VERSION;
    }


    /**
     * The authors of the document. It may be an empty list.
     */
    public Iterable<Author> getAuthors() {
        return _authors;
    }


    /**
     * Randomly generated unique bake identifier. Each bake will have
     * a different identifier.
     */
    public String getBakeId() {
        return _bakeId;
    }


    /**
     * The raw HTML contents of the baked document. This is actually
     * an HTML snippet appropriate for inclusion under a
     * <code>&lt;body&gt;</code> tag, or any other block level
     * element.
     */
    public String getBody() {
        return _body;
    }


    /**
     * The URL of the page being baked relative to the root directory
     * of the site.
     */
    public String getPageUrl() {
        return _pageUrl;
    }


    /**
     * Set of variables available to the page template. These
     * variables are immutable.
     */
    public PageVars getPageVars() {
        return _pageVars;
    }


    /**
     * A fixed string with the Yawg product name.
     */
    public String getProductName() {
        return _productName;
    }


    /**
     * The URL of the root directory of the site being baked relative
     * to the document about to be baked.
     */
    public String getRootRelativeUrl() {
        return _rootRelativeUrl;
    }


    /**
     * The title of the document, as extracted from its source.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * The version of the Yawg software being used.
     */
    public String getVersion() {
        return _version;
    }


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     * Builder of <code>TemplateDataModel</code> instances.
     */
    public static final class Builder {


        private List<Author> _authors;
        private String _body;
        private String _pageUrl;
        private String _rootRelativeUrl;
        private PageVars _pageVars;
        private String _title;


        /**
         *
         */
        private Builder() {

            _authors = new ArrayList<>();
            _body = null;
            _pageUrl = null;
            _rootRelativeUrl = null;
            _pageVars = null;
            _title = null;
        }


        /**
         *
         */
        public Builder addAuthor(
                final String name,
                final String email) {

            Author author = new Author(name, email);

            _authors.add(author);

            return this;
        }


        /**
         *
         */
        public Builder setBody(final String body) {

            _body = Objects.requireNonNull(body);

            return this;
        }


        /**
         *
         */
        public Builder setPageUrl(final String pageUrl) {

            _pageUrl = pageUrl;

            return this;
        }


        /**
         *
         */
        public Builder setRootRelativeUrl(
                final String rootRelativeUrl) {

            _rootRelativeUrl = rootRelativeUrl;

            return this;
        }


        /**
         *
         */
        public Builder setPageVars(final PageVars pageVars) {

            _pageVars = pageVars;

            return this;
        }


        /**
         *
         */
        public Builder setTitle(final String title) {

            _title = Objects.requireNonNull(title);

            return this;
        }


        /**
         *
         */
        public TemplateDataModel build() {

            return new TemplateDataModel(this);
        }


    }


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
