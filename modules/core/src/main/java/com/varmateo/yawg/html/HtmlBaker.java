/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.util.Charsets;


/**
 * An <code>ItemBaker</code> that transforms HTML files into other
 * HTML files.
 */
public final class HtmlBaker
        extends Object
        implements ItemBaker {


    private static final String NAME = "html";

    private static final Pattern RE_ADOC = Pattern.compile(".*\\.html$");

    private static final String TARGET_EXTENSION = ".html";


    /**
     * 
     */
    public HtmlBaker() {

        // Nothing to do.
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getShortName() {

        return NAME;
    }


    /**
     * Checks if the given file name has one of the known extensions.
     *
     * <p>The following extensions will be allowed:</p>
     *
     * <ul>
     *   <li>.adoc</li>
     * </ul>
     *
     * @return True if the given file name has one of the allowed
     * extensions.
     */
    @Override
    public boolean isBakeable(final Path path) {

        String basename = path.getFileName().toString();
        boolean result = RE_ADOC.matcher(basename).matches();

        return result;
    }


    /**
     * Converts the given text file in HTML format into another HTML
     * file processed through the template engine.
     *
     * <p>The target directory must already exist. Otherwise an
     * exception will be thrown.</p>
     *
     * @param sourcePath The file to be baked.
     *
     * @param template Used for generating the target document. If no
     * template is provided, then the source document is just copied
     * to the target directory.
     *
     * @param targetDir The directory where the baked file will be
     * copied to.
     *
     * @exception YawgException Thrown if the baking failed for
     * whatever reason.
     */
    @Override
    public void bake(
            final Path sourcePath,
            final Optional<PageTemplate> template,
            final Path targetDir)
            throws YawgException {

        try {
            doBake(sourcePath, template, targetDir);
        } catch ( IOException e ) {
            YawgException.raise(
                    e,
                    "Failed {0} on {1} - {2} - {3}",
                    NAME,
                    sourcePath,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }
    }


    /**
     *
     */
    private void doBake(
            final Path sourcePath,
            final Optional<PageTemplate> template,
            final Path targetDir)
            throws IOException {

        Path targetPath = getTargetPath(sourcePath, targetDir);

        if ( template.isPresent() ) {
            doBakeWithTemplate(sourcePath, template.get(), targetPath);
        } else {
            doBakeWithoutTemplate(sourcePath, targetPath);
        }
    }


    /**
     *
     */
    private Path getTargetPath(
            final Path sourcePath,
            final Path targetDir) {

        String sourceBasename = sourcePath.getFileName().toString();
        int extensionIndex = sourceBasename.lastIndexOf(".");
        String sourceBasenameNoExtension =
                (extensionIndex>-1)
                ? sourceBasename.substring(0, extensionIndex)
                : sourceBasename;
        String targetBasename = sourceBasenameNoExtension + TARGET_EXTENSION;
        Path targetPath = targetDir.resolve(targetBasename);

        return targetPath;
    }


    /**
     * Baking without a template just copies the source HTML file to
     * the target location.
     */
    private void doBakeWithoutTemplate(
            final Path sourcePath,
            final Path targetPath)
            throws IOException {

        Files.copy(
                sourcePath,
                targetPath,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES);
    }


    /**
     *
     */
    private void doBakeWithTemplate(
            final Path sourcePath,
            final PageTemplate template,
            final Path targetPath)
            throws IOException {

        PageTemplateDataModel dataModel = buildDataModel(sourcePath);

        try ( Writer writer =
                  Files.newBufferedWriter(targetPath, Charsets.UTF_8) ) {
            template.process(dataModel, writer);
        }
    }


    /**
     *
     */
    private PageTemplateDataModel buildDataModel(final Path sourcePath)
            throws IOException {

        Optional<Document> document =
                Optional.of(Jsoup.parse(sourcePath.toFile(), null));
        String body =
                document
                .map(doc -> doc.getElementsByTag("body"))
                .flatMap(elems -> Optional.ofNullable(elems.first()))
                .map(Element::html)
                .orElse("");
        String title =
                document
                .map(doc -> doc.getElementsByTag("title"))
                .flatMap(elems -> Optional.ofNullable(elems.first()))
                .map(Element::text)
                .orElse((String)null);

        PageTemplateDataModel result =
                new PageTemplateDataModel.Builder()
                .setTitle(title)
                .setBody(body)
                .build();

        return result;
    }


}
