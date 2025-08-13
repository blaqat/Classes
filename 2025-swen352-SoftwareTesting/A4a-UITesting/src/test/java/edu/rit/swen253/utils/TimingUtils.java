package edu.rit.swen253.utils;

/**
 * A Utility class for performing timing operations.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class TimingUtils {

    /**
     * Sleep for a few seconds.  Used mostly while debugging tests to see what the UI is doing.
     */
    public static void sleep(final int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    private TimingUtils() {}
}
