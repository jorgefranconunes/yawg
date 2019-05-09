/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import org.junit.Test;

import static com.varmateo.yawg.core.DirBakerConfTestUtils.assertConfEquals;

import com.varmateo.yawg.core.DirBakerConf;


/**
 *
 */
public final class DirBakerConfTest
 {


    /**
     *
     */
    @Test
    public void mergeFromEmpty() {

        final DirBakerConf confEmpty = DirBakerConf.empty();
        final DirBakerConf conf = DirBakerConf.builder()
                .filesToExclude("hello")
                .build()
                .mergeOnTopOf(confEmpty);
        final DirBakerConf expectedConf = DirBakerConf.builder()
                .filesToExclude("hello")
                .build();

        assertConfEquals(expectedConf, conf);
    }


    /**
     *
     */
    @Test
    public void mergeOne() {

        final DirBakerConf conf01 = DirBakerConf.builder()
                .filesToExclude("hello")
                .build();
        final DirBakerConf conf02 = DirBakerConf.builder()
                .filesToExclude("world")
                .build()
                .mergeOnTopOf(conf01);
        final DirBakerConf expectedConf = DirBakerConf.builder()
                .filesToExclude("hello", "world")
                .build();

        assertConfEquals(expectedConf, conf02);
    }


}
