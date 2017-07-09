package jgh.artistex.engine;

import android.graphics.PointF;
import android.graphics.RectF;


/**
 * Class for drawing Rectangles around Layers. Behaviour is similar to
 * <code>RectF</code>, but allows right and left to be interchanged ( and top with bottom).
 * This is useful when dealing with negative scaling of Layers.
 */
public class SpecialRectF {

    /**
     *
     */
    public float left, right, top, bottom;

    public PointF mScaleCenter = new PointF(0, 0);

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public SpecialRectF(float left, float top, float right, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

    }

    /**
     * Sets the left, top, right, bottom fields.
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

    }

    /**
     * Decides if the x,y coordinates are inside this instance
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isInside(float x, float y) {
        float _left = left < right ? left : right;
        float _right = left < right ? right : left;
        float _top = top < bottom ? top : bottom;
        float _bottom = top < bottom ? bottom : top;

        if (_left < x && x < _right && _top < y && y < _bottom) {
            return true;
        }
        return false;
    }

    /**
     * Rotation sign
     *
     * @return
     */
    public int rotSgn() {
        return (int) (Math.signum(this.right - this.left));
    }


    /**
     * Returns the x cooridnate of the center point.
     *
     * @return x coordinate of center
     */
    public float getCenterX() {
        return (left + right) / 2;
    }


    /**
     * Returns the y coordinate of the center point.
     *
     * @return y coordinate of center
     */
    public float getCenterY() {
        return (top + bottom) / 2;
    }


    /**
     * Returns the width of the Rect.
     *
     * @return width
     */
    public float width() {
        return Math.abs(right - left);
    }


    /**
     * Returns the height of the Rect.
     *
     * @return height
     */
    public float height() {
        return Math.abs(bottom - top);
    }


    /**
     * Returns a RectF with the same boundaries as this instance.
     *
     * @return
     */
    public RectF createRectF() {
        RectF r = new RectF();
        float _left = left < right ? left : right;
        float _right = left < right ? right : left;
        float _top = top < bottom ? top : bottom;
        float _bottom = top < bottom ? bottom : top;

        r.set(_left, _top, _right, _bottom);
        return r;
    }

    public float getMaxY(){
        return top > bottom ? top : bottom;
    }

    public float getMinY(){
        return top < bottom ? top : bottom;
    }

    public float getMaxX(){
        return left > right ? left : right;
    }

    public float getMinX(){
        return left < right ? left : right;
    }
}