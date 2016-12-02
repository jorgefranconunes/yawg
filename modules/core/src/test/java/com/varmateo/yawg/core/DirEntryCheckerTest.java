/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirEntryChecker;


/**
 *
 */
public final class DirEntryCheckerTest
 {


    /**
     *
     */
    @Test
    public void withEmptyConf() {

        DirBakerConf conf = DirBakerConf.empty();
        DirEntryChecker checker = new DirEntryChecker(conf);
        Predicate<String> predicate = checker.asStringPredicate();

        assertThat(predicate).accepts("anything");
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

        assertThat(predicate)
                .accepts("something.adoc")
                .rejects("something.txt");
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

        assertThat(predicate)
                .accepts("something.adoc")
                .rejects(
                        "something.adoc~",
                        "something.txt",
                        "something.puml");
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

        assertThat(predicate)
                .accepts("something.adoc")
                .rejects("something.txt");
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

        assertThat(predicate)
                .accepts(
                        "something.adoc",
                        "something.svg")
                .rejects(
                        "something.txt",
                        "something.puml");
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

        assertThat(predicate)
                .accepts("something.adoc")
                .rejects(
                        "something.txt",
                        "something.svg");
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

        assertThat(predicate)
                .accepts(
                        Paths.get("something.adoc"),
                        Paths.get("something.txt/else.adoc"))
                .rejects(
                        Paths.get("something.adoc/else.txt"),
                        Paths.get("something.txt"));
    }


}
