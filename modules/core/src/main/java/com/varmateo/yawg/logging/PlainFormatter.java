/**************************************************************************
 *
 * Copyright (c) 2015-2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;


/**
 * A log formatter that uses a sane format for the log lines.
 */
public final class PlainFormatter
    extends Formatter {





    private static final String DEFAULT_LOG_FMT =
        "{0,date,yyyy-MM.dd HH:mm:ss.SSS} {1} {2}\n";

    private static final String LINE_SEPARATOR =
        System.getProperty("line.separator");

    private final MessageFormat _logFmt;


    /**
     * Initializes the formatter with a default format string.
     */
    public PlainFormatter() {

        _logFmt = new MessageFormat(DEFAULT_LOG_FMT);
    }


    /**
     * Initializes the formatter with the given format string.
     *
     * <p>The format string is expected to follow
     * <code>java.text.MessageFormat</code> conventions. Three
     * formating arguments are used when generating the actual log
     * line. These are the following:</p>
     *
     * <ul>
     *
     *   <li>0 - Date for the log message.</li>
     *
     *   <li>1 - Log level for the log message.</li>
     *
     *   <li>2 - Log message.</li>
     *
     * </ul>
     */
    public PlainFormatter(final String logFmt) {

        _logFmt = new MessageFormat(logFmt);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String format(final LogRecord record) {

        String result = formatLogMessage(record);

        return result;
    }


    /**
     *
     */
    private String formatLogMessage(final LogRecord record) {

        Throwable error    = record.getThrown();
        long      time     = record.getMillis();
        Level     logLevel = record.getLevel();
        String    message  =
            formatLogMessage(time,
                             logLevel,
                             record.getMessage(),
                             record.getParameters());
        String logMessage = null;

        if ( error == null ) {
            logMessage = message;
        } else {
            logMessage =
                formatLogMessageWithError(message, time, logLevel, error);
        }

        return logMessage;
    }


    /**
     *
     */
    private  String formatLogMessage(
            final long     time,
            final Level    logLevel,
            final String   msgFmt,
            final Object[] inputFmtArgs) {

        String msg = null;

        if ( (inputFmtArgs!=null) && (inputFmtArgs.length>0) ) {
            int      count   = inputFmtArgs.length;
            String[] fmtArgs = new String[count];

            for ( int i=0; i<count; ++i ) {
                Object inputFmtArg = inputFmtArgs[i];

                fmtArgs[i] =
                    (inputFmtArg!=null) ? inputFmtArg.toString() : null;
            }
            msg = MessageFormat.format(msgFmt, (Object[])fmtArgs);
        } else {
            msg = msgFmt;
        }

        Date logRecordDate = new Date(time);

        Object[] logMsgFmtArgs = { logRecordDate, logLevel.getName(), msg };
        String logMsg = _logFmt.format(logMsgFmtArgs);

        return logMsg;
    }


    /**
     *
     */
    private String formatLogMessageWithError(
            final String    firstLine,
            final long      time,
            final Level     logLevel,
            final Throwable error) {

        StringWriter buffer = new StringWriter();
        PrintWriter  writer = new PrintWriter(buffer);

        error.printStackTrace(writer);
        buffer.flush();

        StringBuilder msgBuilder = new StringBuilder(firstLine);
        String[]      lines      = buffer.toString().split(LINE_SEPARATOR);

        for ( String line : lines ) {
            String logLine = formatLogMessage(time, logLevel, line, null);

            msgBuilder.append(logLine);
        }

        String logMessage = msgBuilder.toString();

        return logMessage;
    }


}
