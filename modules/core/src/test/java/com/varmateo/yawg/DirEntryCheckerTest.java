/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.DirEntryChecker;


/**
 *
 */
public final class DirEntryCheckerTest
        extends Object {


    /**
     *
     */
    @Test
    public void withEmptyConf() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);

        assertTrue(checker.asStringPredicate().test("anything"));
    }


    /**
     *
     */
    @Test
    public void withIgnoreOne() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIgnore("*.txt")
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<String> predicate = checker.asStringPredicate();

        assertTrue(predicate.test("something.adoc"));
        assertFalse(predicate.test("something.txt"));
    }


    /**
     *
     */
    @Test
    public void withIgnoreMany() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIgnore("*.txt", "*.puml", "*~")
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<String> predicate = checker.asStringPredicate();

        assertTrue(predicate.test("something.adoc"));
        assertFalse(predicate.test("something.adoc~"));
        assertFalse(predicate.test("something.txt"));
        assertFalse(predicate.test("something.puml"));
    }


    /**
     *
     */
    @Test
    public void withIncludeOnlyOne() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIncludeOnly("*.adoc")
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<String> predicate = checker.asStringPredicate();

        assertTrue(predicate.test("something.adoc"));
        assertFalse(predicate.test("something.txt"));
    }


    /**
     *
     */
    @Test
    public void withIncludeOnlyTwo() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIncludeOnly("*.adoc", "*.svg")
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<String> predicate = checker.asStringPredicate();

        assertTrue(predicate.test("something.adoc"));
        assertTrue(predicate.test("something.svg"));
        assertFalse(predicate.test("something.txt"));
        assertFalse(predicate.test("something.puml"));
    }


    /**
     *
     */
    @Test
    public void withIgnoreAndInclude() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIgnore("*.txt")
                .setFilesToIncludeOnly("*.adoc")
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<String> predicate = checker.asStringPredicate();

        assertTrue(predicate.test("something.adoc"));
        assertFalse(predicate.test("something.txt"));
        assertFalse(predicate.test("something.svg"));
    }


    /**
     *
     */
    @Test
    public void pathWithIgnoreOne() {

        DirBakerConf conf =
                new DirBakerConf.Builder()
                .setFilesToIgnore("*.txt")
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<Path> predicate = checker.asPathPredicate();

        assertTrue(predicate.test(Paths.get("something.adoc")));
        assertFalse(predicate.test(Paths.get("something.adoc/else.txt")));
        assertFalse(predicate.test(Paths.get("something.txt")));
        assertTrue(predicate.test(Paths.get("something.txt/else.adoc")));
    }


}