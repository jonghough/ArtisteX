package jgh.artistex.engine.Layers.Pens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import jgh.artistex.engine.IUndo;
import jgh.artistex.engine.IUndoHandler;


/**
 * Vertex controller class controls the vertex points, and control points of
 * <code>Pen</code> objects. <code>VertexController</code> instances control the manipulation
 * of the vertex and control points.
 */
public class VertexController {

    /**
     * Currently manipulating point.
     */
    protected PointF mCurrentPoint = null;


    /**
     * Paint used for Rects representing vertex coordinates.
     */
    protected Paint mPointPaint;


    /**
     * Padding for drawing rects around vertices.
     */
    protected static final int PADDING = 10;


    /**
     * Default Constructor
     */
    public VertexController() {

        mPointPaint = new Paint(Color.BLUE);
    }


    /**
     * Checks if the given <i>(x,y)</i> coordinates lie on the given vertex, with a margin of error
     * given.
     *
     * @param x      x coordiante to test
     * @param y      y coordinate to test
     * @param vertex PointF vertex
     * @param margin margin around PointF point.
     * @return True if on vertex, false otherwise.
     */
    protected boolean isOnVertex(float x, float y, PointF vertex, float margin) {
        if (x > vertex.x - margin && x < vertex.x + margin
                && y > vertex.y - margin && y < vertex.y + margin) {
            return true;
        }
        return false;
    }


    /**
     * Draw the vertex points on the canvas.
     * @param vertexPoints
     * @param canvas
     */
    public void draw(ArrayList<PointF> vertexPoints, Canvas canvas) {

        for (int i = 0; i < vertexPoints.size(); i++) {
            PointF point = vertexPoints.get(i);
            RectF rect = new RectF(point.x - PADDING, point.y - PADDING,
                    point.x + PADDING, point.y + PADDING);

            canvas.drawRect(rect, mPointPaint);
        }
    }


    /**
     * Performs onMotionEvent operation. For pointer down, the next selected vertex is
     * taken, if one exists, depending on whether the pointer down action hit a vertex position.
     * For move events, the vertex is moved to different coordinates, and for up action
     * the vertex is released.
     *
     * @param event     Motion event
     * @param pointList list of vertex points
     */
    /*default*/ IUndo onMotionEvent(MotionEvent event, ArrayList<PointF> pointList) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (PointF point : pointList) {
                if (isOnVertex(event.getX(), event.getY(), point, 45)) {
                    mCurrentPoint = point;
                    VertexMoveUndoHandler handler = new VertexMoveUndoHandler();
                    handler.point = point;
                    handler.xPos = point.x;
                    handler.yPos = point.y;
                    return new IUndo(handler);
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mCurrentPoint == null) return null;
            else {
                mCurrentPoint.set(event.getX(), event.getY());
                return null;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mCurrentPoint = null;
            return null;
        }
        return null;
    }

    /* default */ class VertexMoveUndoHandler implements IUndoHandler{
        public PointF point;
        public float xPos;
        public float yPos;

        @Override
        public void handleUndo() {
            point.set(xPos,yPos);
        }

        @Override
        public void handleRedo() {

        }
    }
}
