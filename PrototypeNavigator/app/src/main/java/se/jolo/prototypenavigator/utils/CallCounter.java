package se.jolo.prototypenavigator.utils;

import android.util.Log;

/**
 * Created by Joel on 2016-02-25.
 */
public final class CallCounter {

    private static int counts = 0;

    public static void count() {
        counts++;
        Log.d("COUNTER", "calls made ::: " + counts);
    }

    public static int getCounts() {
        return counts;
    }
}
