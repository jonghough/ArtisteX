package jgh.artistex.engine.Layers.Pens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import jgh.artistex.engine.SpecialRectF;
import jgh.artistex.engine.IconManager;

/**
 * This class is used to do the RTS transformations of Pens and Pencils.
 * For example, rotating the points, or scaling them etc.
 */
public class PenTransformer {

    /**
     * The Pen object.
     */
    private BasePen mPenObj;


    /**
     *
     */
    private PointF mCenter;


    /**
     *
     */
    private SpecialRectF mBounds;


    /**
     * bounding box paint.
     */
    private Paint mBoundsPaint;

    /**
     * Cache the rotation (in radians) for Undo-ability.
     */
    private float mCachedRotation;


    /**
     * Very small float.
     */
    private static final float EPSILON = 0.001f;


    /**
     * @param pen
     */
    public PenTransformer(BasePen pen) {
        mPenObj = pen;
        mBounds = new SpecialRectF(0, 0, 0, 0);
        mCenter = new PointF();
        mBoundsPaint = new Paint(Color.DKGRAY);
        mBoundsPaint.setStyle(Paint.Style.STROKE);
    }

    public void SetBounds() {
        setCenterAndBounds();
    }

    public float getCurrentRotation() {
        return mCachedRotation;
    }

    /**
     * Sets the center and the bounding rect of the
     * Pen object's vertex points.
     */
    private void setCenterAndBounds() {
        makeBounds();
        int len = mPenObj.getVertexList().size();
        float xtotal = 0;
        float ytotal = 0;
        for (PointF point : mPenObj.getVertexList()) {
            xtotal += point.x;
            ytotal += point.y;
        }
        mCenter.set(xtotal / len, ytotal / len);
    }


    /**
     * Rotates the vertex points and bezier contorl points of the
     * Pen object by the given angle (in radians).
     *
     * @param angle in radians
     */
    public void rotate(float angle) {
        mCachedRotation += angle;
        mCachedRotation = (float) (mCachedRotation % (Math.PI * 2));
        setCenterAndBounds();
        PointF center = mCenter;
        float SINE = mBounds.rotSgn() * (float) Math.sin(angle);
        float COS = (float) Math.cos(angle);

        // rotate the points
        for (PointF point : mPenObj.getVertexList()) {
            float oldRight = point.x - center.x;
            float oldBottom = point.y - center.y;
            point.x = (float) (oldRight * COS - oldBottom * SINE) + center.x;
            point.y = (float) (oldRight * SINE + oldBottom * COS) + center.y;

        }

        for (PointF point : mPenObj.getBezierVertexList()) {
            float oldRight = point.x - center.x;
            float oldBottom = point.y - center.y;
            point.x = (float) (oldRight * COS - oldBottom * SINE) + center.x;
            point.y = (float) (oldRight * SINE + oldBottom * COS) + center.y;

        }
        setCenterAndBounds();
    }


    /**
     * Scales the <code>Pen</code> independently by the given scale
     * amounts in the x and y directions.
     *
     * @param scaleX x scale value
     * @param scaleY y scale value
     */
    public void scale(float scaleX, float scaleY) {
        if (Math.abs(mBounds.left - mBounds.right) < EPSILON)
            return;

        float oldRight = mBounds.right;
        float oldBottom = mBounds.bottom;

        if (mBounds.right < mBounds.left) {

            mBounds.right = scaleX;

        } else {
            mBounds.right = scaleX;

        }
        if (mBounds.bottom < mBounds.top) {

            mBounds.bottom = scaleY;
        } else {
            mBounds.bottom = scaleY;
        }

        for (PointF point : mPenObj.getVertexList()) {
            float newPointX = (mBounds.right - mBounds.left) * (point.x - mBounds.left)
                    / (oldRight - mBounds.left) + mBounds.left;
            float newPointY = (mBounds.bottom - mBounds.top) * (point.y - mBounds.top)
                    / (oldBottom - mBounds.top) + mBounds.top;

            point.x = newPointX;
            point.y = newPointY;

        }

        for (PointF point : mPenObj.getBezierVertexList()) {
            float newPointX = (mBounds.right - mBounds.left) * (point.x - mBounds.left)
                    / (oldRight - mBounds.left) + mBounds.left;
            float newPointY = (mBounds.bottom - mBounds.top) * (point.y - mBounds.top)
                    / (oldBottom - mBounds.top) + mBounds.top;

            point.x = newPointX;
            point.y = newPointY;

        }
        setCenterAndBounds();
    }


    /**
     * Translates the <code>Pen</code> object to center it on the (x,y)
     * point.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    /*default*/ void translate(float x, float y) {
        float oldLeft = mBounds.left;
        float oldRight = mBounds.top;// why oldRight? WTF
        mBounds.left = x;
        mBounds.top = y;

        for (PointF point : mPenObj.getVertexList()) {
            float newPointX = point.x - oldLeft + x;
            float newPointY = point.y - oldRight + y;

            point.x = newPointX;
            point.y = newPointY;

        }

        for (PointF point : mPenObj.getBezierVertexList()) {
            float newPointX = point.x - oldLeft + x;
            float newPointY = point.y - oldRight + y;

            point.x = newPointX;
            point.y = newPointY;

        }

        // This is necessary - especially for the translate undo functionality
        mBounds.right = mBounds.right - oldLeft + x;
        mBounds.bottom = mBounds.bottom - oldRight + y;

        setCenterAndBounds();
    }


    /**
     * Calculates the bounding rect <code>RectF</code> of all the
     * vertex points in the vertex list.
     */
    protected void makeBounds() {
        float left = 0, top = 0, right = 0, bottom = 0;
        if (!mPenObj.getVertexList().isEmpty()) {
            right = left = mPenObj.getVertexList().get(0).x;
            bottom = top = mPenObj.getVertexList().get(0).y;
        }
        if (mPenObj.getVertexList().size() > 1 && Math.abs(mBounds.left - mBounds.right) < 0.0001f) {
            //  mHorizontalPad = 0.1f;
        }
        if (mPenObj.getVertexList().size() > 1 && Math.abs(mBounds.top - mBounds.bottom) < 0.0001f) {
            // mVerticalPad = 0.1f;
        }

        if (mBounds.right < mBounds.left) {
            for (PointF point : mPenObj.getVertexList()) {
                right = point.x < right ? point.x : right;
                left = point.x > left ? point.x : left;

            }

        } else {
            for (PointF point : mPenObj.getVertexList()) {
                left = point.x < left ? point.x : left;
                right = point.x > right ? point.x : right;

            }

        }

        if (mBounds.bottom < mBounds.top) {
            for (PointF point : mPenObj.getVertexList()) {
                bottom = point.y < bottom ? point.y : bottom;
                top = point.y > top ? point.y : top;
            }

        } else {
            for (PointF point : mPenObj.getVertexList()) {
                top = point.y < top ? point.y : top;
                bottom = point.y > bottom ? point.y : bottom;
            }

        }
        mBounds.set(left, top, right, bottom);
        IconManager.instance().setIconCoords(mBounds);
    }


    /**
     * Returns the angle between two points on a circle of radius <code>radius</code>
     *
     * @param p1 the original point
     * @param p2 the moved point
     * @return
     */
    public float getAngleBetweenPoints(PointF p1, PointF p2) {
        float xdiff = p1.x - p2.x;
        float ydiff = p1.y - p2.y;

        float radius = (float) Math.sqrt((p1.x - mCenter.x) * (p1.x - mCenter.x) + (p1.y - mCenter.y) * (p1.y - mCenter.y));
        //need a minimum radius to prevent singularities (i.e. all points collapse into the center)
        if (radius < 0.1f)
            radius = 0.1f;
        //use a quick and dirty cross product to get orientation, direction of movement.
        float orientation = (p2.x - mCenter.x) * (p1.y - mCenter.y) - (p1.x - mCenter.x) * (p2.y - mCenter.y);

        //arc cos argument must be in range [-1,1]
        float arg = 1.0f - (xdiff + ydiff) * (xdiff + ydiff) / (2 * radius * radius);
        if (arg > 1.0f)
            arg = 1.0f;
        else if (arg < -1.0f)
            arg = -1.0f;
        return (float) (Math.signum(-orientation) * Math.acos(arg));
    }


    /**
     * Draw onto the <code>Canvas</code> object.
     *
     * @param canvas drawing canvas.
     */
    /*default*/ void draw(Canvas canvas) {
        makeBounds();
        // if finished don't draw bounding rect.
        if (true) {
            RectF r = new RectF(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom);
            canvas.drawRect(r, mBoundsPaint);
            if (mPenObj.getVertexList() != null && mPenObj.getVertexList().size() > 0) {
                drawIcons(canvas);
            }
        }
    }


    /**
     * Draws the rotate, scale and translate icons onto the canvas.
     *
     * @param canvas the drawing canvas.
     */
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
