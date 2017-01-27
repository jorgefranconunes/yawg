/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import com.varmateo.yawg.BakerService;
import com.varmateo.yawg.BakerServiceFactory;
import com.varmateo.yawg.asciidoctor.AsciidoctorBakerService;


/**
 *
 */
public final class AsciidoctorBakerServiceFactory
        implements BakerServiceFactory {


    private final AsciidoctorBakerService _baker;


    /**
     *
     */
    public AsciidoctorBakerServiceFactory() {

        _baker = new AsciidoctorBakerService();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BakerService newBakerService() {

        return _baker;
    }


}
