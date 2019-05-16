/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import org.junit.Test;

import static com.varmateo.yawg.core.DirBakeOptionsTestUtils.assertConfEquals;

import com.varmateo.yawg.core.DirBakeOptions;


/**
 *
 */
public final class DirBakeOptionsTest
 {


    /**
     *
     */
    @Test
    public void mergeFromEmpty() {

        final DirBakeOptions confEmpty = DirBakeOptions.empty();
        final DirBakeOptions conf = DirBakeOptions.builder()
                .filesToExclude("hello")
                .build()
                .mergeOnTopOf(confEmpty);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .filesToExclude("hello")
                .build();

        assertConfEquals(expectedConf, conf);
    }


    /**
     *
     */
    @Test
    public void mergeOne() {

        final DirBakeOptions conf01 = DirBakeOptions.builder()
                .filesToExclude("hello")
                .build();
        final DirBakeOptions conf02 = DirBakeOptions.builder()
                .filesToExclude("world")
                .build()
                .mergeOnTopOf(conf01);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .filesToExclude("hello", "world")
                .build();

        assertConfEquals(expectedConf, conf02);
    }


}
