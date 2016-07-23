/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.Test;

import static com.varmateo.yawg.DirBakerConfTestUtils.assertConfEquals;

import com.varmateo.yawg.DirBakerConf;


/**
 *
 */
public final class DirBakerConfTest
        extends Object {


    /**
     *
     */
    @Test
    public void mergeFromEmpty() {

        DirBakerConf confEmpty = DirBakerConf.builder().build();
        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToIgnore("hello")
                .build()
                .mergeOnTopOf(confEmpty);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setFilesToIgnore("hello")
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
                .setFilesToIgnore("hello")
                .build();
        DirBakerConf conf02 =
                DirBakerConf.builder()
                .setFilesToIgnore("world")
                .build()
                .mergeOnTopOf(conf01);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setFilesToIgnore("hello", "world")
                .build();

        assertConfEquals(expectedConf, conf02);
    }


}
