/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.util.Arrays;

import static org.junit.Assert.*;
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

        DirBakerConf confEmpty = DirBakerConf.builder().build();
        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToExclude("hello")
                .build()
                .mergeOnTopOf(confEmpty);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setFilesToExclude("hello")
                .build();

        assertConfEquals(expectedConf, conf);
    }


    /**
     *
     */
    @Test
    public void mergeOne() {

        DirBakerConf conf01 =
                DirBakerConf.builder()
                .setFilesToExclude("hello")
                .build();
        DirBakerConf conf02 =
                DirBakerConf.builder()
                .setFilesToExclude("world")
                .build()
                .mergeOnTopOf(conf01);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setFilesToExclude("hello", "world")
                .build();

        assertConfEquals(expectedConf, conf02);
    }


}
