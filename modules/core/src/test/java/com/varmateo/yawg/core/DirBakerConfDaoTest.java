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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
 {


    private final DirBakerConf _emptyConf = DirBakerConf.empty();
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

        assertThat(_emptyConf.templateName.toJavaOptional()).isNotPresent();
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

        assertThatThrownBy(() -> readFromString(confContents))
                .isInstanceOf(YawgException.class);
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

        assertThat(_emptyConf.filesToExclude.toJavaOptional()).isNotPresent();
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

        assertThatThrownBy(() -> readFromString(confContents))
                .isInstanceOf(YawgException.class);
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

        assertThatThrownBy(() -> readFromString(confContents))
                .isInstanceOf(YawgException.class);
    }


    /**
     *
     */
    @Test
    public void withIncludeHereParamOk()
            throws IOException {

        String confContents = ""
                + "includeHere:\n"
                + "  - one\n"
                + "  - two\n";
        DirBakerConf actualConf = readFromString(confContents);
        DirBakerConf expectedConf =
                DirBakerConf.builder()
                .setFilesToIncludeHere("one", "two")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withIncludeHereParamMissing() {

        assertThat(_emptyConf.filesToIncludeHere.toJavaOptional())
                .isNotPresent();
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

        assertThat(value).isPresent();
        assertThat(value).containsInstanceOf(String.class);
        assertThat(value).hasValue("world");
    }


    /**
     *
     */
    @Test
    public void withPageVarsHere()
            throws IOException {

        String confContents = ""
                + "pageVarsHere:\n"
                + "  hello : 'world'\n";
        DirBakerConf actualConf = readFromString(confContents);
        PageVars vars = actualConf.pageVarsHere;
        Optional<Object> value = vars.get("hello");

        assertThat(value).isPresent();
        assertThat(value).containsInstanceOf(String.class);
        assertThat(value).hasValue("world");
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

        assertThatThrownBy(() -> _dao.loadFromFile(noSuchPath))
                .isInstanceOf(YawgException.class);
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
