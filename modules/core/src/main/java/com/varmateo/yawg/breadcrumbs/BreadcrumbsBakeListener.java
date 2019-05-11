/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.breadcrumbs.BreadcrumbItem;
import com.varmateo.yawg.breadcrumbs.Breadcrumbs;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.util.SimpleMap;
import java.util.Optional;


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

        final SimpleMap vars = new SimpleMap(context.pageVars().asMap());
        final Breadcrumbs oldBreadcrumbs = getBreadcrumbs(vars);
        final BreadcrumbItem newBreadcrumbItem = buildBreadcrumbItem(vars, context.dirUrl());
        final Breadcrumbs newBreadcrumbs = extendBreadcrumbs(oldBreadcrumbs, newBreadcrumbItem);

        return updateBreadcrumbs(newBreadcrumbs);
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

        final Optional<SimpleMap> itemData = vars.getMap(VAR_BREADCRUMB);
        final String title = itemData
                .flatMap(m -> m.getString(ATTR_TITLE))
                .orElseGet(() -> basenameOf(dirUrl));
        final String url = itemData
                .flatMap(m -> m.getString(ATTR_URL))
                .map(s -> dirUrl + "/" + s)
                .orElse(dirUrl);

        return new BreadcrumbItem(title, url);
    }


    /**
     * Basename without extension.
     */
    private String basenameOf(final String url) {

        final int index = url.lastIndexOf('/');

        return (index != -1) ? url.substring(index+1) : url;
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

        return PageVarsBuilder.create()
                .addVar(VAR_BREADCRUMB_LIST, newBreadcrumbs)
                .build();
    }


}
