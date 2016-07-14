/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateVars;


/**
 * Interface for objects that want to be notified when one given
 * directory is baked during the baking procedure.
 */
public interface DirBakeListener {


    /**
     * Called when a given directory is being baked during the baking
     * procedure.
     *
     * <p>The listener has the opportunity to update the set of
     * template variables that will be used by the baker when baking
     * the files in the directory.</p>
     *
     * @param context Data on the directory being baked.
     *
     * @return An updated set of template variables to be used by the
     * baker. If the listener does not want to make any change to the
     * template variables it should return the same
     * <code>TemplateVars</code> instance contained in the given
     * <code>PageContext</code>.
     */
    TemplateVars onDirBake(PageContext context);


}
