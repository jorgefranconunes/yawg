/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import com.varmateo.yawg.asciidoctor.AsciidoctorPageBaker;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageBakerFactory;


/**
 *
 */
public final class AsciidoctorPageBakerFactory
        implements PageBakerFactory {


    private final AsciidoctorPageBaker _baker;


    /**
     *
     */
    public AsciidoctorPageBakerFactory() {

        _baker = new AsciidoctorPageBaker();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PageBaker createPageBaker() {

        return _baker;
    }


}
