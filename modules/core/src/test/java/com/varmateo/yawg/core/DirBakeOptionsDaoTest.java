/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;
import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.core.DirBakeOptionsDao;
import com.varmateo.yawg.spi.PageVars;

import static com.varmateo.yawg.core.DirBakeOptionsTestUtils.assertConfEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 *
 */
public final class DirBakeOptionsDaoTest
 {


    private final DirBakeOptions _emptyConf = DirBakeOptions.empty();
    private DirBakeOptionsDao _dao = null;


    @Before
    public void setUp() {

        _dao = new DirBakeOptionsDao();
    }


    /**
     *
     */
    @Test
    public void emptyConf()
            throws IOException {

        final String confContents = "";
        final DirBakeOptions actualConf = readFromString(confContents);
        final DirBakeOptions expectedConf = _emptyConf;

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withTemplateParamOk()
            throws IOException {

        final String templateName = "demo";
        final String confContents = "template: " + templateName;
        final DirBakeOptions actualConf = readFromString(confContents);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .templateName(templateName)
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

        final String confContents = ""
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

        final String confContents = ""
                + "exclude:\n"
                + "  - one\n"
                + "  - .*~\n";
        final DirBakeOptions actualConf = readFromString(confContents);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .filesToExclude("one", ".*~")
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

        final String confContents = ""
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

        final String confContents = ""
                + "exclude: \n"
                + "  - \"[\""; 

        assertThatThrownBy(() -> readFromString(confContents))
                .isInstanceOf(YawgException.class);
    }


    /**
     *
     */
    @Test
    public void withExcludeHereParamOk()
            throws IOException {

        final String confContents = ""
                + "excludeHere:\n"
                + "  - one\n"
                + "  - two\n";
        final DirBakeOptions actualConf = readFromString(confContents);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .filesToExcludeHere("one", "two")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void withIncludeHereParamOk()
            throws IOException {

        final String confContents = ""
                + "includeHere:\n"
                + "  - one\n"
                + "  - two\n";
        final DirBakeOptions actualConf = readFromString(confContents);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .filesToIncludeHere("one", "two")
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

        final String confContents = ""
                + "bakerTypes:\n"
                + "  something :\n"
                + "      - '*.txt'\n"
                + "      - '*.html'\n"
                + "  other :\n"
                +"       - '*.adoc'\n";
        final DirBakeOptions actualConf = readFromString(confContents);
        final DirBakeOptions expectedConf =
                DirBakeOptions.builder()
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

        final String confContents = ""
                + "pageVars:\n"
                + "  hello : 'world'\n";
        final DirBakeOptions actualConf = readFromString(confContents);
        final PageVars vars = actualConf.pageVars;
        final Optional<Object> value = vars.get("hello");

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

        final String confContents = ""
                + "pageVarsHere:\n"
                + "  hello : 'world'\n";
        final DirBakeOptions actualConf = readFromString(confContents);
        final PageVars vars = actualConf.pageVarsHere;
        final Optional<Object> value = vars.get("hello");

        assertThat(value).isPresent();
        assertThat(value).containsInstanceOf(String.class);
        assertThat(value).hasValue("world");
    }


    /**
     *
     */
    @Test
    public void loadFromFileOk() {

        final Path confFile = TestUtils.getInputsDir(DirBakeOptionsDao.class)
                .resolve("dirWithYawgYml/.yawg.yml");
        final DirBakeOptions actualConf = _dao.loadFromFile(confFile);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .templateName("demo")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void loadFromFileNoFile() {

        final Path noSuchPath = Paths.get("this/file/does/not/exist");

        assertThatThrownBy(() -> _dao.loadFromFile(noSuchPath))
                .isInstanceOf(YawgException.class);
    }


    /**
     *
     */
    @Test
    public void loadOk() {

        final Path sourceDir = TestUtils.getInputsDir(DirBakeOptionsDao.class)
                .resolve("dirWithYawgYml");
        final DirBakeOptions actualConf = _dao.loadFromDir(sourceDir);
        final DirBakeOptions expectedConf = DirBakeOptions.builder()
                .templateName("demo")
                .build();

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    @Test
    public void loadNoYawgYml() {

        final Path sourceDir = TestUtils
                .getInputsDir(DirBakeOptionsDao.class)
                .resolve("dirWithNoYawgYml");
        final DirBakeOptions actualConf = _dao.loadFromDir(sourceDir);
        final DirBakeOptions expectedConf = _emptyConf;

        assertConfEquals(expectedConf, actualConf);
    }


    /**
     *
     */
    private DirBakeOptions readFromString(final String confContents)
            throws IOException {

        return _dao.read(new StringReader(confContents));
    }


}
