package jgh.artistex.engine.Layers.Bitmaps;

import android.graphics.Canvas;
import android.graphics.PointF;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.IconManager;

/**
 *　Base class for bitmap layer types.
 */
public abstract class BaseBitmapLayer implements ILayer {


    /**
     * Unique name of this <code>ILayer</code>.
     */
    protected String mName;
    private float mX; // reference coordinate
    private float mY; // reference coordinate
    private float mRotValue = 0; // rotation value
    private float mCachedRotValue = 0; // cached rotation value
    private float mScaleValueX = 1; // horizontal scale
    private float mScaleValueY = 1; // vertical scale

    protected enum Mode {
        ROTATE, SCALE, TRANSLATE, MALLEABLE, NONE;

    }

    protected Mode mMode;



    public BaseBitmapLayer(float x, float y) {
        setCoords(x, y);
        setMode(Mode.NONE);

    }

    public void setCoords(float x, float y) {
        setX(x);
        setY(y);
    }

    public float getX() {
        return mX;
    }

    public void setX(float mX) {
        this.mX = mX;
    }

    public float getY() {
        return mY;
    }

    public void setY(float mY) {
        this.mY = mY;
    }

    public float getRotValue() {
        return mRotValue;
    }

    public void setRotValue(float mRotValue) {
        this.mRotValue = mRotValue;
    }

    public float getCachedRotValue() {
        return mCachedRotValue;
    }

    public void setCachedRotValue(float mCachedRotValue) {
        this.mCachedRotValue = mCachedRotValue;
    }

    public float getScaleValueX() {
        return mScaleValueX;
    }

    public void setScaleValueX(float mScaleValueX) {
        this.mScaleValueX = mScaleValueX;
    }

    public float getScaleValueY() {
        return mScaleValueY;
    }

    public void setScaleValueY(float mScaleValueY) {
        this.mScaleValueY = mScaleValueY;
    }

    protected void setMode(Mode mode) {
        mMode = mode;
    }

    protected synchronized Mode getMode() {
        return mMode;
    }





    /**
     * Returns true if the (x,y) coordinates are inside the icon.
     *
     * @param icon
     * @param touchX
     * @param touchY
     * @return
     */
//    protected boolean isInIcon(LayerIconHolder icon, float touchX, float touchY, int padding) {
//        int width = icon.getBitmap().getWidth();
//        int height = icon.getBitmap().getHeight();
//        if (touchX - padding >= icon.getXCoord() && touchX <= icon.getXCoord() + width + padding
//                && touchY - padding >= icon.getYCoord() && touchY <= icon.getYCoord() + height + padding) { return true; }
//        return false;
//    }



    /**
     * Gets the angle of rotation for performing rotation transformation. Angle
     * is in degrees.
     *
     * @param x x position
     * @param y y position
     * @return angle in degrees
     */
    protected float getRotationAngle(float x, float y) {
        float adjacent, opposite;
        opposite = x - getCenter().x;
        adjacent = y - getCenter().y;

        if (adjacent == 0) {
            return 0;
        }
        float angle = 0;

        float lineslope = opposite / adjacent;
        if (opposite >= 0 && adjacent < 0) {// first quadrant, clockwise
            angle = (float) (Math.toDegrees(Math.atan(Math.abs(lineslope))));
        } else if (opposite > 0 && adjacent > 0) {// second quadrant
            angle = (float) (180 - (Math.toDegrees(Math.atan(Math.abs(lineslope)))));
        } else if (opposite <= 0 && adjacent > 0) {// third quadrant
            angle = (float) (180 + Math.toDegrees(Math.atan(Math.abs(lineslope))));
        } else if (opposite < 0 && adjacent < 0) {// fourth quadrant
            angle = (float) (360 - Math.toDegrees(Math.atan(Math.abs(lineslope))));
        }
        //return (float)Math.toDegrees(Math.atan2(opposite, adjacent));
       return angle;
    }

    /**
     * Gets the angle of rotation about the given point for performing rotation
     * transformation. Angle is in degrees.
     *
     * @param x x position
     * @param y y position
     * @return angle in degrees
     */

    protected float getRotationAngle(float x, float y, PointF center) {
        float adjacent, opposite;
        opposite = x - center.x;
        adjacent = y - center.y;

        if (adjacent == 0) {
            return 0;
        }
        float angle = 0;

        angle = (float)Math.atan2(opposite, adjacent);
        return angle;
    }

    /**
     * Set the icon coordinates. Here the rotate icon is top-left of the
     * mBitmap, Scale is bottom-right, and Translate is top-right.
     */
    protected abstract void setIconCoords();

    protected abstract PointF getCenter();

    protected void drawIcons(Canvas canvas) {
        canvas.drawBitmap(IconManager.instance().getRotationIcon().getBitmap(),
                IconManager.instance().getRotationIcon().getXCoord(),
                IconManager.instance().getRotationIcon().getYCoord(), null);
        canvas.drawBitmap(IconManager.instance().getScaleIcon().getBitmap(),
                IconManager.instance().getScaleIcon().getXCoord(),
                IconManager.instance().getScaleIcon().getYCoord(), null);
        canvas.drawBitmap(IconManager.instance().getTranslateIcon().getBitmap(),
                IconManager.instance().getTranslateIcon().getXCoord(),
                IconManager.instance().getTranslateIcon().getYCoord(), null);
    }

}
