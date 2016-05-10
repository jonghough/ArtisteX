package jgh.artistex.engine.Layers.Pens;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;


/**
 * Controls Bezier point manipulations and drawing.
 * Extension of VertexController.
 */
public class BezierController extends VertexController {


    private Paint mBezierLinePaint;

    /**
     * Instantiate Bezier curve controller
     */
    public BezierController() {
        super();
        mBezierLinePaint = new Paint(Color.BLACK);
        mBezierLinePaint.setStyle(Paint.Style.STROKE);
        mPointPaint = new Paint(Color.DKGRAY);
    }


    /**
     * Draw the bezier points between consecutive vertices of the Pen object. Cubic bezier, so will
     * have two control points between each pair of vertices.
     *
     * @param vertexPoints the vertex list
     * @param bezierPoints the bezier control point list
     * @param canvas       the canvas to draw on.
     */
    public void draw(ArrayList<PointF> vertexPoints, ArrayList<PointF> bezierPoints, Canvas canvas) {

        for (int i = 0; i < bezierPoints.size(); i += 2) {
            PointF point = bezierPoints.get(i);
            PointF next = bezierPoints.get(i + 1);
            RectF rect = new RectF(point.x - PADDING, point.y - PADDING,
                    point.x + PADDING, point.y + PADDING);
            RectF rect2 = new RectF(next.x - PADDING, next.y - PADDING,
                    next.x + PADDING, next.y + PADDING);
            Path line = new Path();
            line.moveTo(vertexPoints.get(i / 2).x, vertexPoints.get(i / 2).y);
            line.lineTo(point.x, point.y);
            line.lineTo(next.x, next.y);
            if (i != bezierPoints.size() - 2) {
                line.lineTo(vertexPoints.get(1 + i / 2).x, vertexPoints.get(1 + i / 2).y);
            }
            canvas.drawPath(line, mBezierLinePaint);
            canvas.drawRect(rect, mPointPaint);
            canvas.drawRect(rect2, mPointPaint);
        }
    }
}




