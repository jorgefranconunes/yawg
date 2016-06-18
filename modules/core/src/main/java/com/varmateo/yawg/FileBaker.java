/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.logging.Log;


/**
 * 
 */
/* package private */ final class FileBaker
        extends Object {


    private final Log _log;
    private final Path _sourceRootDir;
    private final Collection<ItemBaker> _bakers;
    private final ItemBaker _defaultBaker;

    // Keys are baker names (aka baker types), values are the
    // corresponding baker. This map also includes the default baker.
    private final Map<String, ItemBaker> _allBakersByType;


    /**
     * @param log The logger that will be used for logging.
     *
     * @param sourceRootDir The top level directory being baked. This
     * is only used to improve logging messages.
     */
    public FileBaker(
            final Log log,
            final Path sourceRootDir,
            final Collection<ItemBaker> bakers,
            final ItemBaker defaultBaker) {

        _log = log;
        _sourceRootDir = sourceRootDir;
        _bakers = bakers;
        _defaultBaker = defaultBaker;
        _allBakersByType = new HashMap();

        bakers.stream().forEach(b -> _allBakersByType.put(b.getShortName(), b));
        _allBakersByType.put(defaultBaker.getShortName(), defaultBaker);
    }


    /**
     * 
     */
    public void bakeFile(
            final Path sourcePath,
            final Path targetDir,
            final Optional<PageTemplate> template,
            final DirBakerConf dirBakerConf)
            throws YawgException {

        ItemBaker baker = findBaker(sourcePath, dirBakerConf);

        Path sourceRelPath = _sourceRootDir.relativize(sourcePath);
        _log.debug("Baking {0} with {1}", sourceRelPath, baker.getShortName());

        baker.bake(sourcePath, template, targetDir);
    }


    /**
     * @throws YawgException Thrown if the directory configuration
     * specifies a baker type that is unknown.
     */
    private ItemBaker findBaker(
            final Path sourcePath,
            final DirBakerConf dirBakerConf)
            throws YawgException {

        ItemBaker baker =
                dirBakerConf.bakerTypes
                .flatMap(bakerTypes -> bakerTypes.getBakerTypeFor(sourcePath))
                .map(bakerType -> findBakerWithType(bakerType))
                .orElseGet(() -> findBakerFromAll(sourcePath));

        return baker;
    }


    /**
     *
     */
    private ItemBaker findBakerWithType(final String bakerType)
            throws YawgException {

        ItemBaker baker = _allBakersByType.get(bakerType);

        if ( baker == null ) {
            YawgException.raise("Unknown baker type \"{0}\"", bakerType);
        }

        return baker;
    }


    /**
     *
     */
    private ItemBaker findBakerFromAll(final Path sourcePath) {

        ItemBaker baker =
                _bakers.stream()
                .filter(candidate -> candidate.isBakeable(sourcePath))
                .findFirst()
                .orElse(_defaultBaker);

        return baker;
    }


}
