package jgh.artistex.engine.Layers.Pens;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.IUndo;

/**
 *
 */
public abstract class BasePen implements ILayer {

    /**
     * Drawing state enum. The current state of the Pen object.
     */
    public enum State {
        UNUSED, IDLE, DRAWING, SCALING, ROTATING, MOVING, BEZIER, VERTEX
    }


    /**
     * The state.
     */
    protected State mState = State.UNUSED;


    /**
     * Unique name of this <code>ILayer</code>.
     */
    protected String mName;

    /**
     * Fill Paint.
     */
    protected Paint mFillPaint;


    /**
     * Stroke Paint.
     */
    protected Paint mStrokePaint;


    /**
     * The path to draw, when the function <code>draw(Canvas canvas)</code>
     * is called.
     */
    protected Path mPath;


    /**
     * Rotation matrix, for rotating the path.
     */
    protected Matrix mRotMatrix;

    /**
     * Cache the last position, i.e. pointer down or
     * pointer move position.
     */
    protected PointF mLastPosition;


    /**
     * Thickness of the stroke.
     */
    protected int mStrokeThickness;


    /**
     * List of vertices
     */
    protected ArrayList<PointF> mVertexList;


    /**
     * List of bezier points. Generally use cubic bezier points, so two control
     * points between each pair of vertices in <code>mVertexList</code>.
     */
    protected ArrayList<PointF> mBezierVertexList;


    /**
     * Gets the vertex list.
     *
     * @return List of vertices.
     */
    /* default */ ArrayList<PointF> getVertexList() {
        return mVertexList;
    }


    /**
     * Gets the bezier point list.
     *
     * @return List of bezier control points.
     */
    /* default */ ArrayList<PointF> getBezierVertexList() {
        return mBezierVertexList;
    }


    /**
     * Resets the path, redrawing lines between pairs of adjacent vertices, going through the bezier
     * control points.
     */
    protected void resetPath() {
        mPath.reset();

        for (int i = 0; i < mVertexList.size(); i++) {
            PointF p = mVertexList.get(i);
            if (i == 0) {
                mPath.moveTo(p.x, p.y);
            } else {
                PointF b1 = mBezierVertexList.get((i - 1) * 2);
                PointF b2 = mBezierVertexList.get((i - 1) * 2 + 1);
                mPath.cubicTo(b1.x, b1.y, b2.x, b2.y, p.x, p.y);
            }
        }
    }


    /**
     * Sets the state of the Pen
     *
     * @param state state to set.
     */
    public void setState(State state) {
        mState = state;
    }


    /**
     * @param color
     */
    public void setStrokeColor(int color) {
        if (mStrokePaint != null) {
            mStrokePaint.setColor(color);
        }
    }


    /**
     * @param color
     */
    public void setFillColor(int color) {
        if (mFillPaint != null) {
            mFillPaint.setColor(color);
        }
    }

    public void setStrokeThickness(int thickness){
        if(mStrokePaint != null){
            mStrokeThickness = thickness;
            mStrokePaint.setStrokeWidth(thickness);
        }
    }

    public void setCapType(Paint.Cap cap){
        mStrokePaint.setStrokeCap(cap);
    }

    public void setJoinType(Paint.Join join){
        mStrokePaint.setStrokeJoin(join);
    }

    /**
     * Handles the event action down.
     *
     * @param x x coordinate of event
     * @param y y coordinate of event
     * @return <code>IUndo</code> implementation, to make the effect of the
     * action undoable.
     */
    protected abstract IUndo handleDownAction(float x, float y);


    /**
     * Handles the event action move.
     *
     * @param x x coordinate of event
     * @param y y coordinate of event
     * @return <code>IUndo</code> implementation, to make the effect of the
     * action undoable.
     */
    protected abstract IUndo handleMoveAction(float x, float y);


    /**
     * Handles the Action event up.
     *
     * @param x x coordinate of event
     * @param y y coordinate of event
     * @return <code>IUndo</code> implementation, to make the effect of the
     * action undoable.
     */
    protected abstract IUndo handleUpAction(float x, float y);

}
