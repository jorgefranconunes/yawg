/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


/**
 * Utility functions for managing files and directories.
 */
public final class FileUtils
        extends Object {


    /**
     * No instances of this class are to be created.
     */
    private FileUtils() {
    }


    /**
     * Determines the basename of the fibel file.
     *
     * <p>The basename is the name of the file without extension.</p>
     *
     * @param path The path of the file for which the basename will be
     * determined.
     *
     * @return The basename of the given file.
     */
    public static String basename(final Path path) {

        String basenameWithExtension = path.getFileName().toString();
        int extensionIndex = basenameWithExtension.lastIndexOf('.');
        String basenameWithoutExtension =
                (extensionIndex>-1)
                ? basenameWithExtension.substring(0, extensionIndex)
                : basenameWithExtension;

        return basenameWithoutExtension;
    }


    /**
     * Copies one file to a given location.
     *
     * <p>If the target file already exists, itl will be overwritten</p>
     *
     * @param source The path of the source file to be copied.
     *
     * @param target The path of the target file to be created. Its
     * parent directory is suppoed to exist.
     *
     * @throws IOException If there were problems reading from the
     * source file, or writing to the target file.
     */
    public static void copy(
            final Path source,
            final Path target)
            throws IOException {

        Files.copy(
                source,
                target,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES);
    }


    /**
     * Reads the contents of a file into a string.
     *
     * <p>The file is expected to be UTF-8 encoded.</p>
     *
     * @param sourcePath The file to be read.
     *
     * @return The contents of the given file as a string.
     *
     * @throws IOException if there were any problems reading the
     * file.
     */
    public static String readAsString(final Path sourcePath)
            throws IOException {

        byte[] contentBytes = Files.readAllBytes(sourcePath);
        String content = new String(contentBytes, StandardCharsets.UTF_8);

        return content;
    }


    /**
     * Creates a new <code>Writer</code> and passes it to the given
     * consumer.
     *
     * <p>The <code>Writer</code> passed to the consumer is a buffered
     * writer with UTF-8 pointing to the given target file. The
     * <code>Writer</code> is ensured to be closed when this method
     * returns.</p>
     *
     * @param target The file to be written.
     *
     * @param consumer The consumer that is suppoed to write into the
     * <code>Writer</code> associated with the given target file.
     *
     * @throws IOException If there were problems opening the file for
     * writing, or writing into the file.
     */
    public static void newWriter(
            final Path target,
            final ConsumerWithIOException<Writer> consumer)
            throws IOException {

        try ( Writer writer =
                  Files.newBufferedWriter(target, StandardCharsets.UTF_8) ) {
            consumer.accept(writer);
        }
    }


    /**
     * Similar to a <code>java.util.function.Consumer</code> but
     * throws <code>IOException</code>.
     */
    @FunctionalInterface
    public interface ConsumerWithIOException<T> {


        /**
         * Performs the operation on the given argument.
         *
         * @param input The input object.
         *
         * @throws IOException For whatever reason.
         */
        void accept(T input)
                throws IOException;


    }


}