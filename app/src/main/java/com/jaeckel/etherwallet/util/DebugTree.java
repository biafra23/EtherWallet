package com.jaeckel.etherwallet.util;

import android.util.Log;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

/**
 * A {@link Tree Tree} for debug builds. Automatically infers the tag from the calling class.
 */
public class DebugTree extends Timber.Tree {
    private static final int MAX_LOG_LENGTH = 4000;
    private static final int CALL_STACK_INDEX = 5;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
    public static final int STACK_DEPTH = 7;
    public static final int DOT_CLASS = 5;

    /**
     * Break up {@code message} into maximum-length chunks (if needed) and send to either
     * {@link Log#println(int, String, String) Log.println()} or
     * {@link Log#wtf(String, String) Log.wtf()} for logging.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

        Thread currentThread = Thread.currentThread();
        final StackTraceElement trace = currentThread.getStackTrace()[STACK_DEPTH];
        final String filename = trace.getFileName();
        final String linkableSourcePosition = String.format(Locale.GERMAN, "(%s.java:%d)", filename.substring(0, filename.length() - DOT_CLASS), trace.getLineNumber());
        final String logPrefix = String.format("[%s][%s.%s] ", currentThread.getName(), linkableSourcePosition, trace.getMethodName());

        message = logPrefix + message;

        if (message.length() < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message);
            } else {
                Log.println(priority, tag, message);
            }
            return;
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, part);
                } else {
                    Log.println(priority, tag, part);
                }
                i = end;
            } while (i < newline);
        }
    }
}
