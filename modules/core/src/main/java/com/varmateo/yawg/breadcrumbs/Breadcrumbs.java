/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import javaslang.collection.List;

import com.varmateo.yawg.breadcrumbs.BreadcrumbItem;


/**
 *
 */
public final class Breadcrumbs {


    private final List<BreadcrumbItem> _items;


    /**
     *
     */
    private Breadcrumbs() {

        _items = List.of();
    }


    /**
     *
     */
    private Breadcrumbs(final Builder builder) {

        _items = builder._items;
    }


    /**
     *
     */
    public static Breadcrumbs empty() {

        return new Breadcrumbs();
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     * Creates a new builder initialized with the data from the given
     * <code>Breadcrumbs</code>.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder(final Breadcrumbs data) {

        return new Builder(data);
    }


    /**
     *
     */
    public Iterable<BreadcrumbItem> getItems() {

        return _items;
    }


    /**
     *
     */
    public static final class Builder {


        private List<BreadcrumbItem> _items;


        /**
         *
         */
        private Builder() {

            _items = List.of();
        }


        /**
         *
         */
        private Builder(final Breadcrumbs data) {

            _items = data._items;
        }


        /**
         *
         */
        public Builder addBreadcrumbItem(final BreadcrumbItem item) {

            _items = _items.append(item);

            return this;
        }


        /**
         *
         */
        public Breadcrumbs build() {

            return new Breadcrumbs(this);
        }


    }


}
