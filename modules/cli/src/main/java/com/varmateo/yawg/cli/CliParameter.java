/**************************************************************************
 *
 * Copyright (c) 2015-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * A description of a command line option.
 */
public final class CliParameter {

    private final String _shortName;
    private final String _longName;
    private final String _description;
    private final String _argName;

    // Derived from the other attributes.
    private final String _name;
    private final String _literal;
    private final boolean _isWithArg;

    private final Option _apacheOption;

    /**
     * Only for internal use.
     */
    private CliParameter(final Builder builder) {
        _shortName   = builder._shortName;
        _longName    = builder._longName;
        _description = builder._description;
        _argName     = builder._argName;

        _name = (_longName != null) ? _longName : _shortName;
        _literal = (_longName != null) ? ("--" + _longName) : ("-" + _shortName);
        _isWithArg = (_argName != null);

        _apacheOption = buildApacheOption(this);
    }

    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    private static Option buildApacheOption(final CliParameter option) {
        final String longName = option.longName();
        if (longName != null) {
            OptionBuilder.withLongOpt(longName);
        }

        final String description = option.description();
        if (description != null) {
            OptionBuilder.withDescription(description);
        }

        final boolean isWithArg = option.isWithArg();
        if (isWithArg) {
            OptionBuilder.hasArg();
        }

        final String argName = option.argName();
        if (argName != null) {
            OptionBuilder.withArgName(argName);
        }

        final String shortName = option.shortName();

        return (shortName != null)
                ? OptionBuilder.create(shortName)
                : OptionBuilder.create();
    }

    public String shortName() {
        return _shortName;
    }

    public String longName() {
        return _longName;
    }

    public String description() {
        return _description;
    }

    public boolean isWithArg() {
        return _isWithArg;
    }

    /**
     * Informative only.
     */
    public String argName() {
        return _argName;
    }

    public String name() {
        return _name;
    }

    public String literal() {
        return _literal;
    }

    /* default */ Option apacheOption() {
        return _apacheOption;
    }

    /**
     * A builder of <code>CliParameter</code> instances.
     */
    public static final class Builder {

        private String _shortName = null;
        private String _longName = null;
        private String _description = null;
        private String _argName = null;

        private Builder() {
            // Nothing to do.
        }

        public Builder shortName(final String shortName) {
            _shortName = shortName;
            return this;
        }

        public Builder longName(final String longName) {
            _longName = longName;
            return this;
        }

        public Builder description(final String description) {
            _description = description;
            return this;
        }

        public Builder argName(final String argName) {
            _argName = argName;
            return this;
        }

        public CliParameter build() {
            return new CliParameter(this);
        }
    }
}
