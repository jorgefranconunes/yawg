/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import io.vavr.collection.Seq;
import io.vavr.collection.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;
import com.varmateo.yawg.core.DirBaker;
import com.varmateo.yawg.core.DirBakeOptions;
import com.varmateo.yawg.core.FileBaker;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.util.OnDirBakeResults;
import com.varmateo.yawg.util.PageBakeResults;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 */
public final class DirBakerTest {


    private static DirBakeOptions _emptyConf;
    private static DirBakeOptionsDao _confDao;

    private TemplateService _templateServiceMock;
    private DirBakeListener _dirBakeListenerMock;
    private PageBaker _bakerMock;
    private Seq<Path> _bakedFiles;


    /**
     *
     */
    @BeforeClass
    public static void classSetUp() {

        _emptyConf = DirBakeOptions.builder().build();
        _confDao = new DirBakeOptionsDao();
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
                        invocation ->
                        OnDirBakeResults.success(
                                ((PageContext)invocation.getArguments()[0]).pageVars()));

        _bakedFiles = List.empty();

        _bakerMock = mock(PageBaker.class);
        when(_bakerMock.shortName())
                .thenReturn("mock");
        when(_bakerMock.isBakeable(any()))
                .thenReturn(true);
        doAnswer(
                invocation -> {
                    updateBakedFiles((Path)invocation.getArguments()[0]);
                    return PageBakeResults.success();
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

        final DirBakeOptions conf = DirBakeOptions.builder()
                .filesToExclude("*.txt")
                .build();

        final Path sourceDir = TestUtils.getPath(DirBaker.class, "source01");
        final Path targetDir = Paths.get(".");
        final DirBaker baker = buildDirBaker(_bakerMock);

        baker.bakeDirectory(sourceDir, targetDir, conf);

        final Seq<Path> actualBakedFiles = _bakedFiles;
        final Seq<String> expectedBakedFiles = List.of(
                "file02.adoc",
                "file04.adoc");

        assertFileNameEquals(sourceDir, expectedBakedFiles, actualBakedFiles);
    }


    /**
     *
     */
    @Test
    public void bakeSomeWithConfFromDir() {

        final Path sourceDir = TestUtils.getPath(DirBaker.class, "source02");
        final Path targetDir = Paths.get(".");
        final DirBaker baker = buildDirBaker(_bakerMock);

        baker.bakeDirectory(sourceDir, targetDir, _emptyConf);

        final Seq<Path> actualBakedFiles = _bakedFiles;
        final Seq<String> expectedBakedFiles = List.of(
                "file02.adoc",
                "file04.adoc");

        assertFileNameEquals(sourceDir, expectedBakedFiles, actualBakedFiles);
    }


    /**
     *
     */
    private DirBaker buildDirBaker(final PageBaker baker) {

        final FileBaker fileBaker = new FileBaker(List.empty(), baker);

        return new DirBaker(
                fileBaker,
                _templateServiceMock,
                _confDao,
                _dirBakeListenerMock);
    }


    /**
     *
     */
    private void assertFileNameEquals(
            final Path rootDir,
            final Seq<String> expectedRelFiles,
            final Seq<Path> actualFiles) {

        final Seq<String> actualRelFiles = actualFiles
                .map(path -> rootDir.relativize(path))
                .map(Path::toString);

        assertThat(actualRelFiles)
                .isEqualTo(expectedRelFiles);
    }


}
