/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import java.io.OutputStream;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.StreamHandler;

import com.varmateo.yawg.cli.LogInitializer;
import com.varmateo.yawg.logging.PlainFormatter;


/**
 *
 */
final class JulLogInitializer
        implements LogInitializer {


    private static final String LOGGER_NAME = "com.varmateo";

    private static final String LOG_FMT_CONSOLE =
            "{0,date,yyyy-MM.dd HH:mm:ss.SSS} {1} {2}\n";

    private final boolean _isVerbose;
    private final OutputStream _output;

    private Logger _logger;


    /**
     *
     */
    private JulLogInitializer(final Builder builder) {

        _isVerbose = builder._isVerbose;
        _output = builder._output;
    }


    /**
     *
     */
    @Override
    public void init() {

        LogManager.getLogManager().reset();

        Level loggerLevel = _isVerbose ? Level.FINEST : Level.INFO;

        Formatter formatter = new PlainFormatter(LOG_FMT_CONSOLE);

        Handler handler = new StreamHandler(_output, formatter);
        handler.setLevel(loggerLevel);

        String loggerName = LOGGER_NAME;
        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(handler);
        logger.setLevel(loggerLevel);
        logger.setUseParentHandlers(false);

        _logger = logger;
    }


    /**
     *
     */
    @Override
    public void close() {

        for ( Handler handler : _logger.getHandlers() ) {
            handler.flush();
        }
    }


    /**
     *
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     *
     */
    public static final class Builder {


        private boolean _isVerbose;
        private OutputStream _output;


        /**
         *
         */
        private Builder() {

            _isVerbose = false;
            _output = System.out;
        }


        /**
         *
         */
        public Builder setVerbose(final boolean isVerbose) {

            _isVerbose = isVerbose;

            return this;
        }


        /**
         *
         */
        public Builder setOutput(final OutputStream output) {

            _output = output;

            return this;
        }


        /**
         *
         */
        public JulLogInitializer build() {

            return new JulLogInitializer(this);
        }

    }

}
