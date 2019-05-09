/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
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

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToExclude("*.txt")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<String> predicate = checker.asStringPredicate();

        assertThat(predicate)
                .accepts("something.adoc")
                .rejects("something.txt");
    }


    /**
     *
     */
    @Test
    public void withExcludeMany() {

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToExclude("*.txt", "*.puml", "*~")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<String> predicate = checker.asStringPredicate();

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
    public void withExcludeHere() {

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToExcludeHere("*.txt", "*.puml", "*~")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<String> predicate = checker.asStringPredicate();

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
    public void withExcludeAndExcludeHere() {

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToExclude("*.txt")
                .filesToExcludeHere("*.puml", "*~")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<String> predicate = checker.asStringPredicate();

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

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToIncludeHere("*.adoc")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<String> predicate = checker.asStringPredicate();

        assertThat(predicate)
                .accepts("something.adoc")
                .rejects("something.txt");
    }


    /**
     *
     */
    @Test
    public void withIncludeHereTwo() {

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToIncludeHere("*.adoc", "*.svg")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<String> predicate = checker.asStringPredicate();

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

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToExclude("*.txt")
                .filesToIncludeHere("*.adoc")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<String> predicate = checker.asStringPredicate();

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

        final DirBakerConf conf = DirBakerConf.builder()
                .filesToExclude("*.txt")
                .build();
        final DirEntryChecker checker = new DirEntryChecker(conf);
        final Predicate<Path> predicate = checker.asPathPredicate();

        assertThat(predicate)
                .accepts(
                        Paths.get("something.adoc"),
                        Paths.get("something.txt/else.adoc"))
                .rejects(
                        Paths.get("something.adoc/else.txt"),
                        Paths.get("something.txt"));
    }


}
