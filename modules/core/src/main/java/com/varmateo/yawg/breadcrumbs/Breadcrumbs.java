/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import io.vavr.collection.List;


/**
 *
 */
public final class Breadcrumbs {


    // We really need to use a j.u.List because this POJO is used as
    // part of a Freemarker model.
    private final java.util.List<BreadcrumbItem> _items;


    /**
     *
     */
    private Breadcrumbs(final Builder builder) {

        _items = builder._items.toJavaList();
    }


    /**
     *
     */
    public static Breadcrumbs empty() {

        return builder().build();
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
    public java.util.List<BreadcrumbItem> getItems() {

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
        /* default */ Builder() {

            _items = List.of();
        }


        /**
         *
         */
        private Builder(final Breadcrumbs data) {

            _items = List.ofAll(data._items);
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
