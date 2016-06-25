/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.freemarker;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateException;

import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.freemarker.FreemarkerDataModel;


/**
 * Creates templates based on Freemarker template files.
 */
public final class FreemarkerTemplateService
        extends Object
        implements PageTemplateService {


    private final Configuration _fmConfig;
    private final IOException _initializationError;


    /**
     * @param templatesDir The directory containing the Freemarker
     * template files.
     */
    public FreemarkerTemplateService(final Path templatesDir) {

        Configuration fmConfig = null;
        IOException initializationError = null;

        try {
            fmConfig = buildConfiguration(templatesDir);
        } catch ( IOException e ) {
            initializationError = e;
        }

        _fmConfig = fmConfig;
        _initializationError = initializationError;
    }


    /**
     *
     */
    private static Configuration buildConfiguration(final Path templatesDir)
            throws IOException{

        Configuration fmConfig =
                new Configuration(Configuration.VERSION_2_3_24);

        fmConfig.setDirectoryForTemplateLoading(templatesDir.toFile());
        fmConfig.setDefaultEncoding("UTF-8");
        fmConfig.setTemplateExceptionHandler(
                TemplateExceptionHandler.RETHROW_HANDLER);
        fmConfig.setLogTemplateExceptions(false);

        return fmConfig;
    }


    /**
     *
     */
    private void checkInitialization()
            throws YawgException {

        if ( _initializationError != null ) {
            YawgException.raise(
                    _initializationError,
                    "Failed to initialize template service - {0} - {1}",
                    _initializationError.getClass().getSimpleName(),
                    _initializationError.getMessage());
        }
    }


    /**
     *
     */
    @Override
    public PageTemplate getTemplate(final String name)
            throws YawgException {

        checkInitialization();

        Template fmTemplate = getFreemarkerTemplate(name);
        PageTemplate result = new FreemarkerTemplate(fmTemplate);

        return result;
    }


    /**
     *
     */
    private Template getFreemarkerTemplate(final String name)
            throws YawgException {

        Template fmTemplate = null;

        try {
            fmTemplate = _fmConfig.getTemplate(name);
        } catch ( IOException e ) {
            YawgException.raise(
                    e,
                    "Failed to fetch template \"{0}\" - {1} - {2}",
                    name,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        return fmTemplate;
    }


    /**
     *
     */
    private static final class FreemarkerTemplate
            extends Object
            implements PageTemplate {


        private final Template _fmTemplate;


        /**
         *
         */
        public FreemarkerTemplate(final Template fmTemplate) {

            _fmTemplate = fmTemplate;
        }


        /**
         *
         */
        @Override
        public void process(
                final PageTemplateDataModel dataModel,
                final Writer writer)
                throws YawgException {

            FreemarkerDataModel fmDataModel =
                    new FreemarkerDataModel(dataModel);

            try {
                _fmTemplate.process(fmDataModel, writer);
            } catch ( TemplateException | IOException e ) {
                YawgException.raise(
                        e,
                        "Failed to process template \"{0}\" - {1} - {2}",
                        _fmTemplate.getName(),
                        e.getClass().getSimpleName(),
                        e.getMessage());
            }
        }


    }

}
