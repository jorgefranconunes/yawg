/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.breadcrumbs;

import java.util.Map;
import java.util.Optional;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateVars;
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
    public TemplateVars onDirBake(final PageContext context) {

        Breadcrumbs oldBreadcrumbs = getBreadcrumbs(context);
        BreadcrumbItem newBreadcrumbItem = buildBreadcrumbItem(context);
        Breadcrumbs newBreadcrumbs = extendBreadcrumbs(oldBreadcrumbs, newBreadcrumbItem);
        TemplateVars newVars = updateBreadcrumbs(context, newBreadcrumbs);

        return newVars;
    }


    /**
     *
     */
    private Breadcrumbs getBreadcrumbs(final PageContext context) {

        TemplateVars vars = context.templateVars;
        Optional<Breadcrumbs> optBreadcrumbs =
                vars.get(VAR_BREADCRUMB_LIST, Breadcrumbs.class);
        Breadcrumbs result = optBreadcrumbs.orElse(new Breadcrumbs());

        return result;
    }


    /**
     *
     */
    private BreadcrumbItem buildBreadcrumbItem(final PageContext context) {

        TemplateVars vars = context.templateVars;

        Optional<Map<String,Object>> itemData = vars.getMap(VAR_BREADCRUMB);
        String title =
                itemData
                .map(m -> (String)m.get(ATTR_TITLE))
                .orElse(null);
        String url =
                itemData.
                map(m -> (String)m.get(ATTR_URL))
                .orElse((String)null);

        if ( title == null ) {
            title = basenameOf(context.dirUrl);
        }
        if ( url == null ) {
            url = context.dirUrl;
        } else {
            url = context.dirUrl + "/" + url;
        }

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
                new Breadcrumbs.Builder(oldBreadcrumbs)
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
                new TemplateVars.Builder(oldVars)
                .addVar(VAR_BREADCRUMB_LIST, newBreadcrumbs)
                .build();

        return newVars;
    }


}
