package jgh.artistex.engine.Layers.Pens;


import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.IUndo;
import jgh.artistex.engine.IUndoHandler;
import jgh.artistex.engine.LayerType;

/**
 * The <code>Pencil</code> type is similar to the parnet class, <code>Pen</code>, but whereas Pens will add new vertices
 * when the user touches down on the screen, Pencils add new points when user input moves on the screen,
 * giving a smooth motion and smooth curve.
 */
public class Pencil extends Pen {

    public Pencil() {
        mVertexList = new ArrayList<>();
        mBezierVertexList = new ArrayList<>();
        mPath = new Path();
        mStrokePaint = new Paint(Color.GREEN);
        mStrokePaint.setColor(Color.GREEN);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mFillPaint = new Paint();
        mFillPaint.setColor(0x00FFFFFF);
        mTransformer = new PenTransformer(this);
        mRotMatrix = new Matrix();
        mLastPosition = new PointF();
        mBezierController = new BezierController();
        mVertexController = new VertexController();
    }

    private Pencil(Paint strokePaint, Paint fillPaint, ArrayList<PointF> bezierPoints,
                ArrayList<PointF> vertexPoints) {
        mVertexList = new ArrayList<>();
        mBezierVertexList = new ArrayList<>();
        mPath = new Path();
        mStrokePaint = new Paint(strokePaint);
        mFillPaint = new Paint(fillPaint);
        mTransformer = new PenTransformer(this);
        mRotMatrix = new Matrix();
        mLastPosition = new PointF();
        mBezierController = new BezierController();
        for(PointF p : bezierPoints){
            mBezierVertexList.add(new PointF(p.x, p.y));
        }
        for(PointF p : vertexPoints){
            mVertexList.add(new PointF(p.x, p.y));
        }
        mVertexController = new VertexController();
        mSet = true;
    }

    @Override
    public ILayer copy(){
        Pencil pencil = new Pencil(mStrokePaint, mFillPaint, mBezierVertexList, mVertexList);
        return pencil;
    }

    @Override
    protected IUndo handleMoveAction(float x, float y) {
        PointF lastPoint = null;
        if (mVertexList.isEmpty() == false) {
            lastPoint = mVertexList.get(mVertexList.size() - 1);
        }
        final PointF newPoint = new PointF(x, y);
        mVertexList.add(newPoint);
        //bezier
        if (lastPoint != null) {
            final PointF b1 = new PointF(0.6667f * x + 0.3333f * lastPoint.x, 0.6667f * y + 0.3333f * lastPoint.y);

            final PointF b2 = new PointF(0.6667f * lastPoint.x + 0.3333f * x, 0.6667f * lastPoint.y + 0.3333f * y);
            mBezierVertexList.add(b1);
            mBezierVertexList.add(b2);


            return new IUndo(new IUndoHandler() {

                @Override
                public void handleUndo() {
                    mBezierVertexList.remove(b1);

                    mBezierVertexList.remove(b2);

                    mVertexList.remove(newPoint);
                }

                @Override
                public void handleRedo() {

                }
            });
        }
        else{
            // no bezier points to remove.

            return new IUndo(new IUndoHandler() {

                @Override
                public void handleUndo() {

                    mVertexList.remove(newPoint);
                }

                @Override
                public void handleRedo() {

                }
            });
        }
    }


    @Override
    public IUndo onEvent(MotionEvent event) {

        float x, y;
        x = event.getX();
        y = event.getY();

        if(!mSet) {
            mSet = true;
            mTransformer.translate(x, y);
        }

        switch (mState) {
            case UNUSED:
            case IDLE:
                mState = State.DRAWING;
                break;
            case DRAWING:
                mLastPosition.set(x, y);
                mTransformer.SetBounds();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return handleDownAction(x, y);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return handleMoveAction(x, y);
                }

                break;
            case ROTATING:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mState = State.IDLE;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float angle = mTransformer.getAngleBetweenPoints(mLastPosition, new PointF(x, y));
                    mTransformer.rotate(angle);
                    mLastPosition.set(x, y);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //nothing.
                    mLastPosition.set(x, y);

                }
                break;
            case SCALING:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mState = State.IDLE;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mTransformer.scale(x, y);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                break;
            case MOVING:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mState = State.IDLE;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mTransformer.translate(x, y);

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                break;
            case BEZIER:
                return mBezierController.onMotionEvent(event, mBezierVertexList);
            case VERTEX:
                return mVertexController.onMotionEvent(event, mVertexList);
            default:
                break;
        }
        return null;
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.PENCIL;
    }
}
