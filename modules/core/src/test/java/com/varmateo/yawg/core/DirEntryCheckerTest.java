/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirEntryChecker;


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

        DirBakerConf conf = DirBakerConf.builder().build();
        DirEntryChecker checker = new DirEntryChecker(conf);

        assertTrue(checker.asStringPredicate().test("anything"));
    }


    /**
     *
     */
    @Test
    public void withExcludeOne() {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToExclude("*.txt")
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
    public void withExcludeMany() {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToExclude("*.txt", "*.puml", "*~")
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
    public void withIncludeHereOne() {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToIncludeHere("*.adoc")
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
    public void withIncludeHereTwo() {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToIncludeHere("*.adoc", "*.svg")
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
    public void withExcludeAndInclude() {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToExclude("*.txt")
                .setFilesToIncludeHere("*.adoc")
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
    public void pathWithExcludeOne() {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToExclude("*.txt")
                .build();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<Path> predicate = checker.asPathPredicate();

        assertTrue(predicate.test(Paths.get("something.adoc")));
        assertFalse(predicate.test(Paths.get("something.adoc/else.txt")));
        assertFalse(predicate.test(Paths.get("something.txt")));
        assertTrue(predicate.test(Paths.get("something.txt/else.adoc")));
    }


}
