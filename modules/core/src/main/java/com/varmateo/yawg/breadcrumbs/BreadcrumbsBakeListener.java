/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import java.util.Optional;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.breadcrumbs.BreadcrumbItem;
import com.varmateo.yawg.breadcrumbs.Breadcrumbs;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.util.SimpleMap;


/**
 *
 */
public final class BreadcrumbsBakeListener
        implements DirBakeListener {


    private static final String VAR_BREADCRUMB = "breadcrumb";
    private static final String VAR_BREADCRUMB_LIST = "breadcrumbs";

    private static final String ATTR_TITLE = "title";
    private static final String ATTR_URL = "url";


    /**
     *
     */
    @Override
    public PageVars onDirBake(final PageContext context)
            throws YawgException {

        SimpleMap vars = new SimpleMap(context.getPageVars().asMap());
        Breadcrumbs oldBreadcrumbs = getBreadcrumbs(vars);
        BreadcrumbItem newBreadcrumbItem =
                buildBreadcrumbItem(vars, context.getDirUrl());
        Breadcrumbs newBreadcrumbs =
                extendBreadcrumbs(oldBreadcrumbs, newBreadcrumbItem);
        PageVars newVars = updateBreadcrumbs(newBreadcrumbs);

        return newVars;
    }


    /**
     *
     */
    private Breadcrumbs getBreadcrumbs(final SimpleMap vars) {

        return vars.get(VAR_BREADCRUMB_LIST, Breadcrumbs.class)
                .orElseGet(Breadcrumbs::empty);
    }


    /**
     *
     */
    private BreadcrumbItem buildBreadcrumbItem(
            final SimpleMap vars,
            final String dirUrl) {

        Optional<SimpleMap> itemData = vars.getMap(VAR_BREADCRUMB);
        String title =
                itemData
                .flatMap(m -> m.getString(ATTR_TITLE))
                .orElseGet(() -> basenameOf(dirUrl));
        String url =
                itemData
                .flatMap(m -> m.getString(ATTR_URL))
                .map(s -> dirUrl + "/" + s)
                .orElse(dirUrl);

        return new BreadcrumbItem(title, url);
    }


    /**
     * Basename without extension.
     */
    private String basenameOf(final String url) {

        int index = url.lastIndexOf('/');

        return (index!=-1) ? url.substring(index+1) : url;
    }


    /**
     *
     */
    private Breadcrumbs extendBreadcrumbs(
            final Breadcrumbs oldBreadcrumbs,
            final BreadcrumbItem newBreadcrumbItem) {

        return Breadcrumbs.builder(oldBreadcrumbs)
                .addBreadcrumbItem(newBreadcrumbItem)
                .build();
    }


    /**
     *
     */
    private PageVars updateBreadcrumbs(final Breadcrumbs newBreadcrumbs) {

        return PageVars.builder()
                .addVar(VAR_BREADCRUMB_LIST, newBreadcrumbs)
                .build();
    }


}
