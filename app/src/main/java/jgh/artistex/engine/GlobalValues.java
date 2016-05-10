package jgh.artistex.engine;

import android.graphics.Paint;

/**
 *
 */
/* default */ class GlobalValues {

    /**
     * current stroke color. Default is black
     */
    static int strokeColor = 0xFF000000;

    /**
     * current fill color. Default, clear.
     */
    static int fillColor = 0X00000000;

    /**
     * current stroke thickness
     */
    static int strokeThickness = 1;

    /**
     * current join type
     */
    static Paint.Join joinType;

    /**
     * current cap type
     */
    static Paint.Cap capType;

    /**
     * current background color. Default is white.
     */
    static int backgroundColor = 0xFFFFFFFF;

    /**
     * Padding around the Rotation, Scale, Translate icons
     * to give more chance of user pressing them.
     */
    static float iconPadding = 55.0f;

    /**
     * Flag for showing the transform box around the current
     * layer. Transform box allows RTS transformations. If not
     * shown, then transformations are not possible.
     */
    static boolean showTransformBox = true;
}
