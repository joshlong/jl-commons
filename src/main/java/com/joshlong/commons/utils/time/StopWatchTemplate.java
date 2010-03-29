package com.joshlong.commons.utils.time;

import org.apache.commons.lang.time.StopWatch;

/**
 * A utility class that allows easy testing and timing of a task.
 *
 * @author <a href="mailto:josh@Joshlong.com">Josh Long</a>
 */
public class StopWatchTemplate {
    /**
     * @param stopWatchCallback the callback thats to be timed.
     *
     * @return how many milliseconds the task took.
     *
     * @throws Throwable any exception that you will throw.
     */
    public long execute(StopWatchCallback stopWatchCallback) throws Throwable {

        if (null == stopWatchCallback) {
            throw new NullPointerException("stopWatchCallback is null!");
        }

        StopWatch sw = new StopWatch();
        sw.start();
        stopWatchCallback.execute();
        sw.stop();
        return sw.getTime();
    }
}

