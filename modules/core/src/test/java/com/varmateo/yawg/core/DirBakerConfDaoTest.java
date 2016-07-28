/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.YawgException;

import static com.varmateo.yawg.core.DirBakerConfTestUtils.assertConfEquals;
import com.varmateo.yawg.core.DirBakerConfDao;
import com.varmateo.yawg.PageVars;


/**
 *
 */
public final class DirBakerConfDaoTest
        extends Object {


    private final DirBakerConf _emptyConf = DirBakerConf.builder().build();
    private DirBakerConfDao _dao = null;


    @Before
    public void setUp() {

        _dao = new DirBakerConfDao();
    }


    /**
     *
     */
    @Test
    public void emptyConf()
            throws IOException {

        String confContents = "";
        DirBakerConf actualConf = readFromString(confContents);
        DirBakerConf expectedConf = _emptyConf;

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withTemplateParamOk()
            throws IOException {

        String templateName = "demo";
        String confContents = ""
                + "template: " + templateName;
        DirBakerConf actualConf = readFromString(confContents);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setTemplateName(templateName)
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withTemplateParamMissing() {

        assertFalse(_emptyConf.templateName.isPresent());
    }


    /**
     *
     */
    @Test
    public void withTemplateParamFail()
            throws IOException {

        String confContents = ""
                + "template: \n"
                + "  - something: wrong";

        TestUtils.assertThrows(
                YawgException.class,
                () -> readFromString(confContents));
    }


    /**
     *
     */
    @Test
    public void withExcludeParamOk()
            throws IOException {

        String confContents = ""
                + "exclude:\n"
                + "  - one\n"
                + "  - .*~\n";
        DirBakerConf actualConf = readFromString(confContents);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setFilesToExclude("one", ".*~")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withExcludeParamMissing()
            throws IOException {

        assertFalse(_emptyConf.filesToExclude.isPresent());
    }


    /**
     *
     */
    @Test
    public void withExcludeParamFail()
            throws IOException {

        String confContents = ""
                + "exclude: \n"
                + "  - something: wrong"; 

        TestUtils.assertThrows(
                YawgException.class,
                () -> readFromString(confContents));
    }


    /**
     *
     */
    @Test
    public void withExcludeParamInvalidRegex()
            throws IOException {

        String confContents = ""
                + "exclude: \n"
                + "  - \"[\""; 

        TestUtils.assertThrows(
                YawgException.class,
                () -> readFromString(confContents));
    }


    /**
     *
     */
    @Test
    public void withIncludeOnlyParamOk()
            throws IOException {

        String confContents = ""
                + "includeOnly:\n"
                + "  - one\n"
                + "  - two\n";
        DirBakerConf actualConf = readFromString(confContents);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setFilesToIncludeOnly("one", "two")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withIncludeOnlyParamMissing() {

        assertFalse(_emptyConf.filesToIncludeOnly.isPresent());
    }


    /**
     *
     */
    @Test
    public void withBakerTypesParamOk()
            throws IOException {

        String confContents = ""
                + "bakerTypes:\n"
                + "  something :\n"
                + "      - '*.txt'\n"
                + "      - '*.html'\n"
                + "  other :\n"
                +"       - '*.adoc'\n";
        DirBakerConf actualConf = readFromString(confContents);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .addBakerType("something", "*.txt", "*.html")
                .addBakerType("other", "*.adoc")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withPageVars()
            throws IOException {

        String confContents = ""
                + "pageVars:\n"
                + "  hello : 'world'\n";
        DirBakerConf actualConf = readFromString(confContents);
        PageVars vars = actualConf.pageVars;
        Optional<Object> value = vars.get("hello");

        assertTrue(value.isPresent());
        assertTrue(value.get() instanceof String);
        assertEquals("world", value.get());
    }


    /**
     *
     */
    @Test
    public void loadFromFileOk() {

        Path confFile =
                TestUtils
                .getInputsDir(DirBakerConfDao.class)
                .resolve("dirWithYawgYml/.yawg.yml");
        DirBakerConf actualConf = _dao.loadFromFile(confFile);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setTemplateName("demo")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void loadFromFileNoFile() {

        Path noSuchPath = Paths.get("this/file/does/not/exist");

        TestUtils.assertThrows(
                YawgException.class,
                () -> _dao.loadFromFile(noSuchPath));
    }


    /**
     *
     */
    @Test
    public void loadOk() {

        Path sourceDir =
                TestUtils
                .getInputsDir(DirBakerConfDao.class)
                .resolve("dirWithYawgYml");
        DirBakerConf actualConf = _dao.loadFromDir(sourceDir);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setTemplateName("demo")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void loadNoYawgYml() {

        Path sourceDir =
                TestUtils
                .getInputsDir(DirBakerConfDao.class)
                .resolve("dirWithNoYawgYml");
        DirBakerConf actualConf = _dao.loadFromDir(sourceDir);
        DirBakerConf expectedConf = _emptyConf;

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    private DirBakerConf readFromString(final String confContents)
            throws IOException {

        DirBakerConf result = _dao.read(new StringReader(confContents));

        return result;
    }


}
