package com.joshlong.commons.utils.time;

/**
 * Provides a place to inject the logic that should be executed
 *
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public interface StopWatchCallback {
    void execute() throws Throwable;
}
