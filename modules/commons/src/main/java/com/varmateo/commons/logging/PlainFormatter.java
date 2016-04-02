/**************************************************************************
 *
 * Copyright (c) 2015-2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.commons.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;






/***************************************************************************
 *
 * A log formatter that uses a sane format for the log lines.
 *
 ***************************************************************************/

public final class PlainFormatter
    extends Formatter {





    private static final String LOG_FMT_STR =
        "{0,date,yyyy-MM.dd HH:mm:ss.SSS} {1} {2}\n";

    private static final MessageFormat LOG_FMT = new MessageFormat(LOG_FMT_STR);

    private static final String LINE_SEPARATOR =
        System.getProperty("line.separator");





/***************************************************************************
 *
 *
 *
 ***************************************************************************/

    public PlainFormatter() {

        // Nothing to do.
    }





/***************************************************************************
 *
 * {@inheritDoc}
 *
 ***************************************************************************/

    @Override
    public String format(final LogRecord record) {

        String result = formatLogMessage(record);

        return result;
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private static String
        formatLogMessage(final LogRecord record) {

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





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private static Date _logRecordDate = new Date();

    private static synchronized String
        formatLogMessage(final long     time,
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

        _logRecordDate.setTime(time);

        Object[] logMsgFmtArgs = { _logRecordDate, logLevel.getName(), msg };
        String   logMsg        = LOG_FMT.format(logMsgFmtArgs);
                
        return logMsg;
    }





/**************************************************************************
 *
 * 
 *
 **************************************************************************/

    private static String formatLogMessageWithError(final String    firstLine,
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





/***************************************************************************
 *
 *
 *
 ***************************************************************************/

