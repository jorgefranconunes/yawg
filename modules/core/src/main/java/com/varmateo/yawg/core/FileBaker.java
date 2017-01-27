/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.collection.Seq;
import javaslang.control.Option;

import com.varmateo.yawg.BakerService;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.util.Exceptions;


/**
 * 
 */
/* package private */ final class FileBaker {


    private final Log _log;
    private final Seq<BakerService> _bakers;
    private final BakerService _defaultBaker;

    // Keys are baker names (aka baker types), values are the
    // corresponding bakers. This map also includes the default baker.
    private final Map<String, BakerService> _allBakersByType;


    /**
     * @param log The logger that will be used for logging.
     */
    FileBaker(
            final Log log,
            final Seq<BakerService> bakers,
            final BakerService defaultBaker) {

        _log = log;
        _bakers = bakers;
        _defaultBaker = defaultBaker;
        _allBakersByType =
                HashMap.ofEntries(
                        bakers.map(b -> Tuple.of(b.getShortName(), b)))
                .put(defaultBaker.getShortName(), defaultBaker);
    }


    /**
     * 
     */
    public void bakeFile(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final DirBakerConf dirBakerConf)
            throws YawgException {

        BakerService baker = findBaker(sourcePath, dirBakerConf);
        long startTime = System.currentTimeMillis();

        baker.bake(sourcePath, context, targetDir);

        long delay = System.currentTimeMillis() - startTime;
        Path sourceBasename = sourcePath.getFileName();
        _log.debug(
                "    {1}: {0} ({2}ms)",
                sourceBasename,
                baker.getShortName(),
                String.valueOf(delay));
    }


    /**
     * @throws YawgException Thrown if the directory configuration
     * specifies a baker type that is unknown.
     */
    private BakerService findBaker(
            final Path sourcePath,
            final DirBakerConf dirBakerConf)
            throws YawgException {

        BakerService baker =
                dirBakerConf.bakerTypes
                .flatMap(bakerTypes -> bakerTypes.getBakerTypeFor(sourcePath))
                .map(bakerType -> findBakerWithType(bakerType))
                .getOrElse(() -> findBakerFromAll(sourcePath));

        return baker;
    }


    /**
     *
     */
    private BakerService findBakerWithType(final String bakerType)
            throws YawgException {

        Option<BakerService> baker = _allBakersByType.get(bakerType);

        if ( !baker.isDefined() ) {
            throw Exceptions.raise("Unknown baker type \"{0}\"", bakerType);
        }

        return baker.get();
    }


    /**
     *
     */
    private BakerService findBakerFromAll(final Path sourcePath) {

        return _bakers
                .filter(candidate -> candidate.isBakeable(sourcePath))
                .headOption()
                .getOrElse(_defaultBaker);
    }


}
