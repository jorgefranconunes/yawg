/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
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

        DirBakerConf confEmpty = new DirBakerConf.Builder().build();
        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIgnore("hello")
                .build()
                .mergeOnTopOf(confEmpty);
        DirBakerConf expectedConf =
                new DirBakerConf.Builder()
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
                new DirBakerConf.Builder()
                .setFilesToIgnore("hello")
                .build();
        DirBakerConf conf02 =
                new DirBakerConf.Builder()
                .setFilesToIgnore("world")
                .build()
                .mergeOnTopOf(conf01);
        DirBakerConf expectedConf =
                new DirBakerConf.Builder()
                .setFilesToIgnore("hello", "world")
                .build();

        assertConfEquals(expectedConf, conf02);
    }


}