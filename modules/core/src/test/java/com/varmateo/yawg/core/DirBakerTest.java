/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateService;

import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.FileBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;


/**
 *
 */
public final class DirBakerTest {


    private static DirBakerConf _emptyConf;
    private static DirBakerConfDao _confDao;

    private TemplateService _templateServiceMock;
    private DirBakeListener _dirBakeListenerMock;
    private Baker _bakerMock;
    private List<Path> _bakedFiles;


    /**
     *
     */
    @BeforeClass
    public static void classSetUp() {

        _emptyConf = DirBakerConf.builder().build();
        _confDao = new DirBakerConfDao();
    }


    /**
     *
     */
    @Before
    public void setUp() {

        _templateServiceMock = mock(TemplateService.class);
        when(_templateServiceMock.getTemplate(anyString()))
                .thenReturn(Optional.empty());

        _dirBakeListenerMock = mock(DirBakeListener.class);
        when(_dirBakeListenerMock.onDirBake(any(PageContext.class)))
                .thenAnswer(
                        invocation -> ((PageContext)invocation.getArguments()[0]).getPageVars());

        _bakedFiles = new ArrayList<>();
        _bakerMock = mock(Baker.class);
        when(_bakerMock.getShortName()).thenReturn("mock");
        when(_bakerMock.isBakeable(any())).thenReturn(true);
        doAnswer(
                invocation -> {
                    _bakedFiles.add((Path)invocation.getArguments()[0]);
                    return null;
                })
                .when(_bakerMock).bake(any(), any(), any());
    }


    /**
     *
     */
    @Test
    public void bakeSomeWithConfFixed() {

        DirBakerConf conf =
                DirBakerConf.builder()
                .setFilesToExclude("*.txt")
                .build();

        Path sourceDir = TestUtils.getPath(DirBaker.class, "source01");
        Path targetDir = Paths.get(".");
        DirBaker baker = buildDirBaker(sourceDir, targetDir, _bakerMock);

        baker.bakeDirectory(sourceDir, targetDir, conf);

        List<Path> actualBakedFiles = _bakedFiles;
        List<String> expectedBakedFiles =
                Arrays.asList(
                        "file02.adoc",
                        "file04.adoc");

        assertFileNameEquals(sourceDir, expectedBakedFiles, actualBakedFiles);
    }


    /**
     *
     */
    @Test
    public void bakeSomeWithConfFromDir() {

        Path sourceDir = TestUtils.getPath(DirBaker.class, "source02");
        Path targetDir = Paths.get(".");
        DirBaker baker = buildDirBaker(sourceDir, targetDir, _bakerMock);

        baker.bakeDirectory(sourceDir, targetDir, _emptyConf);

        List<Path> actualBakedFiles = _bakedFiles;
        List<String> expectedBakedFiles =
                Arrays.asList(
                        "file02.adoc",
                        "file04.adoc");

        assertFileNameEquals(sourceDir, expectedBakedFiles, actualBakedFiles);
    }


    /**
     *
     */
    private DirBaker buildDirBaker(
            final Path sourceRootDir,
            final Path targetRootDir,
            final Baker baker) {

        Log log = LogFactory.createFor(DirBaker.class);
        FileBaker fileBaker =
                new FileBaker(
                        log,
                        Collections.emptyList(),
                        baker);
        DirBaker result =
                new DirBaker(
                        log,
                        sourceRootDir,
                        targetRootDir,
                        fileBaker,
                        _templateServiceMock,
                        _confDao,
                        _dirBakeListenerMock);
        return result;
    }


    /**
     *
     */
    private void assertFileNameEquals(
            final Path rootDir,
            final List<String> expectedRelFiles,
            final List<Path> actualFiles) {

        List<String> actualRelFiles =
                actualFiles.stream()
                .map(path -> rootDir.relativize(path))
                .map(Path::toString)
                .collect(Collectors.toList());

        assertThat(actualRelFiles).isEqualTo(expectedRelFiles);
    }


}
