/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.varmateo.yawg.breadcrumbs.BreadcrumbItem;
import com.varmateo.yawg.util.Lists;


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
        public Builder(final Breadcrumbs data) {

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
