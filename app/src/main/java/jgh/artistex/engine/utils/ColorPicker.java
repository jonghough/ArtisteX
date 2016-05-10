package jgh.artistex.engine.utils;

/**
 *
 */
public class ColorPicker {


    /**
     * Gets the alpha component of the ARGB int.
     * Returned value will be in range [0,255]
     *
     * @param color ARGB int
     * @return alpha int
     */
    public static int getAlpha(int color) {
        return (color >> 24) & 0xFF;
    }


    /**
     * Gets the red component of the ARGB int.
     * Returned value will be in range [0,255]
     *
     * @param color ARGB int
     * @return red int
     */
    public static int getRed(int color) {
        return (color & 0x00FF0000) >> 16;
    }


    /**
     * Gets the green component of the ARGB int.
     * Returned value will be in range [0,255]
     *
     * @param color ARGB int
     * @return green int
     */
    public static int getBlue(int color) {
        return (color & 0x0000FF00) >> 8;
    }


    /**
     * Gets the blue component of the ARGB int.
     * Returned value will be in range [0,255]
     *
     * @param color ARGB int
     * @return blue int
     */
    public static int getGreen(int color) {
        return color & 0x000000FF;
    }


    /**
     * Sets the alpha value fo the given color (ARGB int).
     *
     * @param color
     * @param alpha
     * @return
     */
    public static int setAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }


    /**
     * Sets the red value fo the given color (ARGB int).
     *
     * @param color
     * @param red
     * @return
     */
    public static int setRed(int color, int red) {
        return (color & 0xFF00FFFF) | (red << 16);
    }


    /**
     * Sets the green value fo the given color (ARGB int).
     *
     * @param color
     * @param green
     * @return
     */
    public static int setGreen(int color, int green) {
        return (color & 0xFFFF00FF) | (green << 8);
    }


    /**
     * Sets the blue value fo the given color (ARGB int).
     *
     * @param color
     * @param blue
     * @return
     */
    public static int setBlue(int color, int blue) {
        return (color & 0xFFFFFF00) | blue;
    }
}
