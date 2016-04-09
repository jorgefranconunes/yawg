/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.logging.Log;


/**
 * 
 */
/* package private */ final class FileBaker
        extends Object
        implements ItemBaker {


    private static final String NAME = "main";

    private final Log _log;
    private final Path _sourceRootDir;
    private final Collection<ItemBaker> _bakers;
    private final ItemBaker _defaultBaker;


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
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getShortName() {

        return NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBakeable(final Path path) {

        boolean result = Files.isRegularFile(path);

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void bake(
            final Path sourcePath,
            final Optional<PageTemplate> template,
            final Path targetDir)
            throws YawgException {

        ItemBaker baker = findBaker(sourcePath);

        Path sourceRelPath = _sourceRootDir.relativize(sourcePath);
        _log.debug("Baking {0} with {1}", sourceRelPath, baker.getShortName());

        baker.bake(sourcePath, template, targetDir);
    }


    /**
     *
     */
    private ItemBaker findBaker(final Path sourcePath) {

        ItemBaker baker = null;

        for ( ItemBaker candidateBaker : _bakers ) {
            if ( candidateBaker.isBakeable(sourcePath) ) {
                baker = candidateBaker;
                break;
            }
        }

        if ( baker == null ) {
            baker = _defaultBaker;
        }

        return baker;
    }


}
