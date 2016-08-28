/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.MockTemplate;
import com.varmateo.yawg.asciidoctor.AsciidoctorBaker;


/**
 *
 */
public final class AsciidoctorBakerTest
        extends Object {


    private AsciidoctorBaker _baker = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _baker = new AsciidoctorBaker();
    }


    /**
     *
     */
    @Test
    public void checkIsBakeable() {

        Path pathBakeable = Paths.get("SomethingBakeable.adoc");
        assertTrue(_baker.isBakeable(pathBakeable));

        Path pathNonBakeable = Paths.get("SomethingNotBakeable.txt");
        assertFalse(_baker.isBakeable(pathNonBakeable));
    }


    /**
     *
     */
    @Test
    public void missingSourceFileNoTemplate() {

        Path sourcePath = Paths.get("this-file-does-not-exist.adoc");
        PageContext context =
                PageContext.builder()
                .setDirUrl(".")
                .setRootRelativeUrl(".")
                .build();
        Path targetDir = Paths.get("does-not-matter");

        TestUtils.assertThrows(
                YawgException.class,
                () -> _baker.bake(sourcePath, context, targetDir));
    }


    /**
     *
     */
    @Test
    public void missingSourceFileWithTemplate() {

        Path sourcePath = Paths.get("this-file-does-not-exist.adoc");
        MockTemplate template = new MockTemplate();
        PageContext context =
                PageContext.builder()
                .setDirUrl(".")
                .setRootRelativeUrl(".")
                .setTemplateFetcher(path -> Optional.of(template))
                .build();
        Path targetDir = Paths.get("does-not-matter");

        TestUtils.assertThrows(
                YawgException.class,
                () -> _baker.bake(sourcePath, context, targetDir));
    }


    /**
     *
     */
    @Test
    public void noDiagramNoTemplate()
            throws IOException {

        Path targetDir =
                TestUtils.getTmpDir(AsciidoctorBaker.class);

        assertCreatedFiles(
                "no-diagram.adoc",
                Optional.empty(),
                targetDir,
                "no-diagram.html");
    }


    /**
     *
     */
    @Test
    public void withDiagramNoTemplate()
            throws IOException {

        Path targetDir =
                TestUtils.getTmpDir(AsciidoctorBaker.class);

        assertCreatedFiles(
                "with-diagram.adoc",
                Optional.empty(),
                targetDir,
                "with-diagram.html",
                "diagram.svg");
    }


    /**
     *
     */
    @Test
    public void withDiagramWithTemplate()
            throws IOException {

        Path targetDir =
                TestUtils.getTmpDir(AsciidoctorBaker.class);
        Template template =
                new MockTemplate();

        assertCreatedFiles(
                "with-diagram.adoc",
                Optional.of(template),
                targetDir,
                "with-diagram.html",
                "diagram.svg");
    }


    /**
     *
     */
    @Test
    public void withDiagramNoTemplateRelativeTargetDir()
            throws IOException {

        Path targetDir =
                TestUtils.getTmpDir(AsciidoctorBaker.class);
        Path currentWorkingDir =
                Paths.get(".").toAbsolutePath().normalize();
        Path targetDirRel =
                currentWorkingDir.relativize(targetDir);

        assertCreatedFiles(
                "with-diagram.adoc",
                Optional.empty(),
                targetDirRel,
                "with-diagram.html",
                "diagram.svg");
    }


    /**
     *
     */
    @Test
    public void withDiagramWithTemplateRelativeTargetDir()
            throws IOException {

        Path targetDir =
                TestUtils.getTmpDir(AsciidoctorBaker.class);
        Path currentWorkingDir =
                Paths.get(".").toAbsolutePath().normalize();
        Path targetDirRel =
                currentWorkingDir.relativize(targetDir);
        Template template =
                new MockTemplate();

        assertCreatedFiles(
                "with-diagram.adoc",
                Optional.of(template),
                targetDirRel,
                "with-diagram.html",
                "diagram.svg");
    }


    /**
     *
     */
    private void assertCreatedFiles(
            final String sourceFile,
            final Optional<Template> template,
            final Path targetDir,
            final String... expectedFiles) {

        Path sourcePath = TestUtils.getPath(AsciidoctorBaker.class, sourceFile);
        PageContext context =
                PageContext.builder()
                .setDirUrl(".")
                .setRootRelativeUrl(".")
                .setTemplateFetcher(path -> template)
                .build();

        _baker.bake(sourcePath, context, targetDir);

        for ( String expectedFile : expectedFiles ) {
            Path expectedPath = targetDir.resolve(expectedFile);
            assertTrue(
                    "Missing file \"" +
                    expectedPath.toAbsolutePath().normalize() +
                    "\"",
                    Files.exists(expectedPath));
        }

    }


}
