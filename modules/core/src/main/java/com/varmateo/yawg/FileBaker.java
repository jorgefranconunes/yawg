/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.logging.Log;


/**
 * 
 */
/* package private */ final class FileBaker
        extends Object {


    private final Log _log;
    private final Path _sourceRootDir;
    private final Collection<Baker> _bakers;
    private final Baker _defaultBaker;

    // Keys are baker names (aka baker types), values are the
    // corresponding bakers. This map also includes the default baker.
    private final Map<String, Baker> _allBakersByType;


    /**
     * @param log The logger that will be used for logging.
     *
     * @param sourceRootDir The top level directory being baked.
     */
    public FileBaker(
            final Log log,
            final Path sourceRootDir,
            final Collection<Baker> bakers,
            final Baker defaultBaker) {

        _log = log;
        _sourceRootDir = sourceRootDir;
        _bakers = bakers;
        _defaultBaker = defaultBaker;
        _allBakersByType = new HashMap<>();

        bakers.stream().forEach(b -> _allBakersByType.put(b.getShortName(), b));
        _allBakersByType.put(defaultBaker.getShortName(), defaultBaker);
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

        Baker baker = findBaker(sourcePath, dirBakerConf);
        Path sourceRelPath = _sourceRootDir.relativize(sourcePath);
        long startTime = System.currentTimeMillis();

        baker.bake(sourcePath, context, targetDir);

        long delay = System.currentTimeMillis() - startTime;
        _log.debug("    {0} with {1} ({2}ms)",
                   sourceRelPath,
                   baker.getShortName(),
                   String.valueOf(delay));
    }


    /**
     * @throws YawgException Thrown if the directory configuration
     * specifies a baker type that is unknown.
     */
    private Baker findBaker(
            final Path sourcePath,
            final DirBakerConf dirBakerConf)
            throws YawgException {

        Baker baker =
                dirBakerConf.bakerTypes
                .flatMap(bakerTypes -> bakerTypes.getBakerTypeFor(sourcePath))
                .map(bakerType -> findBakerWithType(bakerType))
                .orElseGet(() -> findBakerFromAll(sourcePath));

        return baker;
    }


    /**
     *
     */
    private Baker findBakerWithType(final String bakerType)
            throws YawgException {

        Baker baker = _allBakersByType.get(bakerType);

        if ( baker == null ) {
            YawgException.raise("Unknown baker type \"{0}\"", bakerType);
        }

        return baker;
    }


    /**
     *
     */
    private Baker findBakerFromAll(final Path sourcePath) {

        Baker baker =
                _bakers.stream()
                .filter(candidate -> candidate.isBakeable(sourcePath))
                .findFirst()
                .orElse(_defaultBaker);

        return baker;
    }


}
