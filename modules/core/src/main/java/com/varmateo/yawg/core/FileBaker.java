/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageContext;


/**
 * 
 */
/* package private */ final class FileBaker {


    private final Log _log;
    private final Seq<PageBaker> _bakers;
    private final PageBaker _defaultBaker;

    // Keys are baker names (aka baker types), values are the
    // corresponding bakers. This map also includes the default baker.
    private final Map<String, PageBaker> _allBakersByType;


    /**
     * @param log The logger that will be used for logging.
     */
    FileBaker(
            final Seq<PageBaker> bakers,
            final PageBaker defaultBaker) {

        _log = LogFactory.createFor(FileBaker.class);
        _bakers = bakers;
        _defaultBaker = defaultBaker;
        _allBakersByType = HashMap.ofEntries(bakers.map(b -> Tuple.of(b.shortName(), b)))
                .put(defaultBaker.shortName(), defaultBaker);
    }


    /**
     * 
     */
    public void bakeFile(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final DirBakeOptions dirBakeOptions)
            throws YawgException {

        final PageBaker baker = findBaker(sourcePath, dirBakeOptions);
        final long startTime = System.currentTimeMillis();

        baker.bake(sourcePath, context, targetDir);

        final long delay = System.currentTimeMillis() - startTime;
        final Path sourceBasename = sourcePath.getFileName();
        _log.debug(
                "    {1}: {0} ({2}ms)", sourceBasename, baker.shortName(), String.valueOf(delay));
    }


    /**
     * @throws YawgException Thrown if the directory configuration
     * specifies a baker type that is unknown.
     */
    private PageBaker findBaker(
            final Path sourcePath,
            final DirBakeOptions dirBakeOptions)
            throws YawgException {

        return dirBakeOptions.bakerTypes
                .flatMap(bakerTypes -> bakerTypes.bakerTypeFor(sourcePath))
                .map(bakerType -> findBakerWithType(bakerType))
                .getOrElse(() -> findBakerFromAll(sourcePath));
    }


    /**
     *
     */
    private PageBaker findBakerWithType(final String bakerType)
            throws YawgException {

        final Option<PageBaker> baker = _allBakersByType.get(bakerType);

        if ( !baker.isDefined() ) {
            throw FileBakerException.unknownBakerType(bakerType);
        }

        return baker.get();
    }


    /**
     *
     */
    private PageBaker findBakerFromAll(final Path sourcePath) {

        return _bakers
                .filter(candidate -> candidate.isBakeable(sourcePath))
                .headOption()
                .getOrElse(_defaultBaker);
    }


}
