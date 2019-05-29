/**************************************************************************
 *
 * Copyright (c) 2017-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.spi.TemplateDataModel.Author;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Builder of <code>TemplateDataModel</code> instances.
 */
public final class TemplateDataModelBuilder {


    private List<Author> _authors;
    private String _body;
    private String _pageUrl;
    private String _rootRelativeUrl;
    private PageVars _pageVars;
    private String _title;
    private String _bakeId;


    /**
     *
     */
    private TemplateDataModelBuilder() {

        _authors = new ArrayList<>();
        _body = null;
        _pageUrl = null;
        _rootRelativeUrl = null;
        _pageVars = PageVars.empty();
        _title = null;
        _bakeId = null;
    }


    /**
     * Creates a new empty builder.
     *
     * @return A newly created <code>TemplateDataModelBuilder</code>
     * instance.
     */
    public static TemplateDataModelBuilder create() {

        return new TemplateDataModelBuilder();
    }


    /**
     *
     */
    public TemplateDataModelBuilder addAuthor(
            final String name,
            final String email) {

        Author author = Author.create(name, email);
        _authors.add(author);
        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setBody(final String body) {

        _body = Objects.requireNonNull(body);
        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setPageUrl(final String pageUrl) {

        _pageUrl = pageUrl;
        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setRootRelativeUrl(
            final String rootRelativeUrl) {

        _rootRelativeUrl = rootRelativeUrl;
        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setPageVars(final PageVars pageVars) {

        _pageVars = pageVars;
        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder setTitle(final String title) {

        _title = Objects.requireNonNull(title);
        return this;
    }


    /**
     *
     */
    public TemplateDataModelBuilder bakeId(final String bakeId) {

        _bakeId = bakeId;
        return this;
    }


    /**
     *
     */
    public TemplateDataModel build() {

        return new TemplateDataModelImpl(this);
    }


    /**
     *
     */
    private static final class TemplateDataModelImpl
        implements TemplateDataModel {


        private final List<Author> _authors;
        private final String _body;
        private final String _pageUrl;
        private final PageVars _pageVars;
        private final String _rootRelativeUrl;
        private final String _title;
        private final String _bakeId;


        /**
         *
         */
        TemplateDataModelImpl(final TemplateDataModelBuilder builder) {

            _authors = Collections.unmodifiableList(new ArrayList<>(builder._authors));
            _body = Objects.requireNonNull(builder._body);
            _pageUrl = Objects.requireNonNull(builder._pageUrl);
            _pageVars = Objects.requireNonNull(builder._pageVars);
            _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
            _title = Objects.requireNonNull(builder._title);
            _bakeId = Objects.requireNonNull(builder._bakeId);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Iterable<Author> authors() {
            return _authors;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String body() {
            return _body;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String pageUrl() {
            return _pageUrl;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public PageVars pageVars() {
            return _pageVars;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String rootRelativeUrl() {
            return _rootRelativeUrl;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String title() {
            return _title;
        }


        /**
         *
         */
        @Override
        public String bakeId() {
            return _bakeId;
        }

    }

}
