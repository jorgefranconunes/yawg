/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.varmateo.yawg.commons.util.Lists;

import com.varmateo.yawg.breadcrumbs.BreadcrumbItem;


/**
 *
 */
public final class Breadcrumbs
        extends Object {


    private final List<BreadcrumbItem> _items;


    /**
     *
     */
    public Breadcrumbs() {

        _items = Collections.emptyList();
    }


    /**
     *
     */
    private Breadcrumbs(final Builder builder) {

        _items = Lists.readOnlyCopy(builder._items);
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
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

        Builder result = new Builder(data);

        return result;
    }


    /**
     *
     */
    public List<BreadcrumbItem> getItems() {

        return _items;
    }


    /**
     *
     */
    public static final class Builder
            extends Object {


        private List<BreadcrumbItem> _items;


        /**
         *
         */
        private Builder() {

            _items = new ArrayList<>();
        }


        /**
         *
         */
        private Builder(final Breadcrumbs data) {

            _items = new ArrayList<>(data._items);
        }


        /**
         *
         */
        public Builder addBreadcrumbItem(final BreadcrumbItem item) {

            _items.add(item);

            return this;
        }


        /**
         *
         */
        public Breadcrumbs build() {

            Breadcrumbs result = new Breadcrumbs(this);

            return result;
        }


    }


}
