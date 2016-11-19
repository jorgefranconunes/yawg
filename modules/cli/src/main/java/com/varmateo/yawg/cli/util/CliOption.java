/**************************************************************************
 *
 * Copyright (c) 2015-2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli.util;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;


/**
 * A description of a command line option.
 */
public final class CliOption {


    private String  _shortName   = null;
    private String  _longName    = null;
    private String  _description = null;
    private String  _argName     = null;

    // Derived from the other attributes.
    private String  _name = null;
    private String  _literal = null;
    private boolean _isWithArg = false;

    private Option  _apacheOption = null;


    /**
     * Only for internal use.
     */
    private CliOption(final Builder builder) {

        _shortName   = builder._shortName;
        _longName    = builder._longName;
        _description = builder._description;
        _argName     = builder._argName;

        _name = (_longName!=null) ? _longName : _shortName;
        _literal = (_longName!=null) ? ("--"+_longName) : ("-"+_shortName);
        _isWithArg = (_argName!=null);

        _apacheOption = buildApacheOption(this);
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
    }


    /**
     *
     */
    private static Option buildApacheOption(final CliOption option) {

        Option result = null;

        String longName = option.getLongName();
        if ( longName != null ) {
            OptionBuilder.withLongOpt(longName);
        }

        String description = option.getDescription();
        if ( description != null ) {
            OptionBuilder.withDescription(description);
        }

        boolean isWithArg = option.isWithArg();
        if ( isWithArg ) {
            OptionBuilder.hasArg();
        }

        String argName = option.getArgName();
        if ( argName != null ) {
            OptionBuilder.withArgName(argName);
        }

        String shortName = option.getShortName();
        if ( shortName != null ) {
            result = OptionBuilder.create(shortName);
        } else {
            result = OptionBuilder.create();
        }

        return result;
    }


    /**
     *
     */
    public String getShortName() {

        return _shortName;
    }


    /**
     *
     */
    public String getLongName() {

        return _longName;
    }


    /**
     *
     */
    public String getDescription() {

        return _description;
    }


    /**
     *
     */
    public boolean isWithArg() {

        return _isWithArg;
    }


    /**
     * Informative only.
     */
    public String getArgName() {

        return _argName;
    }


    /**
     *
     */
    public String getName() {

        return _name;
    }


    /**
     *
     */
    public String getLiteral() {

        return _literal;
    }


    /**
     *
     */
    /* package private */ Option apacheOption() {

        return _apacheOption;
    }


    /**
     * A builder of <code>CliOption</code> instances.
     */
    public static final class Builder
 {


        private String  _shortName   = null;
        private String  _longName    = null;
        private String  _description = null;
        private String  _argName     = null;


        /**
         *
         */
        private Builder() {
            // Nothing to do.
        }


        /**
         *
         */
        public Builder setShortName(final String shortName) {

            _shortName = shortName;

            return this;
        }


        /**
         *
         */

        public Builder setLongName(final String longName) {

            _longName = longName;

            return this;
        }



        /**
         *
         */

        public Builder setDescription(final String description) {

            _description = description;

            return this;
        }


        /**
         *
         */
        public Builder setArgName(final String argName) {

            _argName = argName;

            return this;
        }


        /**
         *
         */
        public CliOption build() {

            CliOption result = new CliOption(this);

            return result;
        }


    }


}
