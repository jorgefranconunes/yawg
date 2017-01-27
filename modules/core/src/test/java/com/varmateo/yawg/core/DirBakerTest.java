/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javaslang.collection.Seq;
import javaslang.collection.List;

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

import com.varmateo.yawg.BakerService;
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
    private BakerService _bakerMock;
    private Seq<Path> _bakedFiles;


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

        _bakedFiles = List.of();
        _bakerMock = mock(BakerService.class);
        when(_bakerMock.getShortName()).thenReturn("mock");
        when(_bakerMock.isBakeable(any())).thenReturn(true);
        doAnswer(
                invocation -> {
                    updateBakedFiles((Path)invocation.getArguments()[0]);
                    return null;
                })
                .when(_bakerMock).bake(any(), any(), any());
    }


    private void updateBakedFiles(final Path path) {

        _bakedFiles = _bakedFiles.append(path);
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

        Seq<Path> actualBakedFiles = _bakedFiles;
        Seq<String> expectedBakedFiles =
                List.of(
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

        Seq<Path> actualBakedFiles = _bakedFiles;
        Seq<String> expectedBakedFiles =
                List.of(
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
            final BakerService baker) {

        Log log = LogFactory.createFor(DirBaker.class);
        FileBaker fileBaker =
                new FileBaker(
                        log,
                        List.of(),
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
            final Seq<String> expectedRelFiles,
            final Seq<Path> actualFiles) {

        Seq<String> actualRelFiles =
                actualFiles
                .map(path -> rootDir.relativize(path))
                .map(Path::toString);

        assertThat(actualRelFiles).isEqualTo(expectedRelFiles);
    }


}
