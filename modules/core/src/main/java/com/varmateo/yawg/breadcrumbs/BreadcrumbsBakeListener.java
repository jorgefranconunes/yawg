/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import java.util.Optional;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateVars;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.util.SimpleMap;

import com.varmateo.yawg.breadcrumbs.BreadcrumbItem;
import com.varmateo.yawg.breadcrumbs.Breadcrumbs;


/**
 *
 */
public final class BreadcrumbsBakeListener
        extends Object
        implements DirBakeListener {


    private static final String VAR_BREADCRUMB = "breadcrumb";
    private static final String VAR_BREADCRUMB_LIST = "breadcrumbList";

    private static final String ATTR_TITLE = "title";
    private static final String ATTR_URL = "url";


    /**
     *
     */
    public BreadcrumbsBakeListener() {
        // Nothing to do.
    }


    /**
     *
     */
    @Override
    public TemplateVars onDirBake(final PageContext context)
            throws YawgException {

        SimpleMap vars = new SimpleMap(context.templateVars.asMap());
        Breadcrumbs oldBreadcrumbs = getBreadcrumbs(vars);
        BreadcrumbItem newBreadcrumbItem =
                buildBreadcrumbItem(vars, context.dirUrl);
        Breadcrumbs newBreadcrumbs =
                extendBreadcrumbs(oldBreadcrumbs, newBreadcrumbItem);
        TemplateVars newVars = updateBreadcrumbs(context, newBreadcrumbs);

        return newVars;
    }


    /**
     *
     */
    private Breadcrumbs getBreadcrumbs(final SimpleMap vars) {

        Optional<Breadcrumbs> optBreadcrumbs =
                vars.get(VAR_BREADCRUMB_LIST, Breadcrumbs.class);
        Breadcrumbs result = optBreadcrumbs.orElse(new Breadcrumbs());

        return result;
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

        BreadcrumbItem result = new BreadcrumbItem(title, url);

        return result;
    }


    /**
     *
     */
    private String basenameOf(final String url) {

        int index = url.lastIndexOf('/');
        String result = (index!=-1) ? url.substring(index+1) : url;

        return result;
    }


    /**
     *
     */
    private Breadcrumbs extendBreadcrumbs(
            final Breadcrumbs oldBreadcrumbs,
            final BreadcrumbItem newBreadcrumbItem) {

        Breadcrumbs newBreadcrumbs =
                Breadcrumbs.builder(oldBreadcrumbs)
                .addBreadcrumbItem(newBreadcrumbItem)
                .build();

        return newBreadcrumbs;
    }


    /**
     *
     */
    private TemplateVars updateBreadcrumbs(
            final PageContext context,
            final Breadcrumbs newBreadcrumbs) {

        TemplateVars oldVars = context.templateVars;
        TemplateVars newVars =
                TemplateVars.builder(oldVars)
                .addVar(VAR_BREADCRUMB_LIST, newBreadcrumbs)
                .build();

        return newVars;
    }


}
