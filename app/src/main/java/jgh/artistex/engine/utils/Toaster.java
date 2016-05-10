package jgh.artistex.engine.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Make Toast messages.
 */
public class Toaster {

    /**
     * Makes a short duration toast message
     *
     * @param activity Activity
     * @param message  message id.
     */
    public static void makeShortToast(Activity activity, int message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Makes a short duration toast message
     *
     * @param activity Activity
     * @param message  message string.
     */
    public static void makeShortToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

    }


    /**
     * Makes a long duration toast message
     *
     * @param activity Activity
     * @param message  message id.
     */
    public static void makeLongToast(Activity activity, int message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Makes a long duration toast message
     *
     * @param activity Activity
     * @param message  message string.
     */
    public static void makeLongToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

    }
}
