/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.BakerFactory;
import com.varmateo.yawg.asciidoctor.AsciidoctorBaker;


/**
 *
 */
public final class AsciidoctorBakerFactory
        extends Object
        implements BakerFactory {


    private final AsciidoctorBaker _baker;


    /**
     *
     */
    public AsciidoctorBakerFactory() {

        _baker = new AsciidoctorBaker();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Baker newBaker() {

        return _baker;
    }


}
