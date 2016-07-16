/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;


/**
 *
 */
public final class BreadcrumbItem
        extends Object {


    private final String _title;
    private final String _url;


    /**
     *
     */
    public BreadcrumbItem(
            final String title,
            final String url) {

        _title = title;
        _url = url;
    }


    /**
     *
     */
    public String getTitle() {
        return _title;
    }


    /**
     *
     */
    public String getUrl() {
        return _url;
    }


}
