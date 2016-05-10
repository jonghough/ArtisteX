package jgh.artistex.engine;

import android.graphics.Bitmap;

/**
 * A class for holding bitmaps and related information for the Rotation, Scale and Translation
 */
public final class LayerIconHolder {

    /**
     * the x coordinate to draw the bitmap on canvas.
     */
    private float xCoord;


    /**
     * the y coordinate to draw the bitmap on canvas.
     */
    private float yCoord;


    /**
     * The icon's bitmap.
     */
    private Bitmap mBitmap;

    /**
     *
     */
    private boolean mShowing = true;

    /*
     * Constructors
     */

    /**
     * Constructor taking a bitmap as the parameter. (x,y) coordinates will not
     * be set.
     *
     * @param bitmap
     */
    public LayerIconHolder(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    /**
     * Constructor taking a bitymap and (x,y) coordinates as parameters.
     *
     * @param bitmap
     * @param x
     * @param y
     */
    public LayerIconHolder(Bitmap bitmap, float x, float y) {
        mBitmap = bitmap;
        this.initializeCoords(x, y);
    }

    /**
     * Initialize the (x,y) coordinates.
     *
     * @param x
     * @param y
     */
    /* default */ void initializeCoords(float x, float y) {
        setXCoord(x);
        setYCoord(y);
    }

    /**
     * Get the x coordinate
     *
     * @return
     */
    public float getXCoord() {
        return xCoord - mBitmap.getWidth() / 2;
    }

    /**
     * Set the x coordinate
     *
     * @param xCoord
     */
    public void setXCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    /**
     * Gets the y coordinate
     *
     * @return
     */
    public float getYCoord() {
        return yCoord - mBitmap.getHeight() / 2;
    }

    /**
     * Sets the y coordinate
     *
     * @param yCoord
     */
    public void setYCoord(float yCoord) {
        this.yCoord = yCoord;
    }

    /**
     * Gets the bitmap
     *
     * @return
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * Sets the bitmap
     *
     * @param mBitmap
     */
    void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setShowing(boolean showing){
        mShowing = showing;
    }

    /**
     * Tests if the given (x,y) coordinate is inside the bounds of the icon (given by the bitmap's
     * height and width parameters).
     *
     * @param xcoord  x coordinate
     * @param ycoord  y coordinate
     * @return true if the (x,y) coordinate is <i>on</i> the icon's bitmap, false otherwise.
     */
    public boolean isInIcon(float xcoord, float ycoord) {
        if(!mShowing)
            return false;
        else {
            float padding = GlobalValues.iconPadding;
            int width = getBitmap().getWidth();
            int height = getBitmap().getHeight();
            if (xcoord >= getXCoord() - padding && xcoord <= getXCoord() + width + padding
                    && ycoord >= getYCoord() - padding && ycoord <= getYCoord() + height + padding) {
                return true;
            }
            return false;
        }
    }

}