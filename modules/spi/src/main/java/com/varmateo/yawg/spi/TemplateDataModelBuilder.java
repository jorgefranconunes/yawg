/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
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


    /**
     *
     */
    private TemplateDataModelBuilder() {

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


        /**
         *
         */
        TemplateDataModelImpl(final TemplateDataModelBuilder builder) {

            _authors =
                Collections.unmodifiableList(new ArrayList<>(builder._authors));
            _body = Objects.requireNonNull(builder._body);
            _pageUrl = Objects.requireNonNull(builder._pageUrl);
            _pageVars =
                (builder._pageVars!=null) ? builder._pageVars: PageVars.empty();
            _rootRelativeUrl = Objects.requireNonNull(builder._rootRelativeUrl);
            _title = Objects.requireNonNull(builder._title);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public Iterable<Author> getAuthors() {
            return _authors;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getBody() {
            return _body;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getPageUrl() {
            return _pageUrl;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public PageVars getPageVars() {
            return _pageVars;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getRootRelativeUrl() {
            return _rootRelativeUrl;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public String getTitle() {
            return _title;
        }

    }

}
