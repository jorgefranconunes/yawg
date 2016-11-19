/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;


/**
 * Attributes of a breadcrumb item.
 */
public final class BreadcrumbItem {


    private final String _title;
    private final String _url;


    /**
     * @param title The title for this breadcrumb item.
     *
     * @param url The URL for this breadcrum item.
     */
    public BreadcrumbItem(
            final String title,
            final String url) {

        _title = title;
        _url = url;
    }


    /**
     * @return The title of this breadcrumb item.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * @return The URL for this breadcrum item.
     */
    public String getUrl() {
        return _url;
    }


}
