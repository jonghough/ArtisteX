package jgh.artistex.engine.Layers.Pens;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.visitors.ILayerVisitor;
import jgh.artistex.engine.IUndo;
import jgh.artistex.engine.IUndoHandler;
import jgh.artistex.engine.IconManager;
import jgh.artistex.engine.LayerType;
import jgh.artistex.engine.utils.DataUtils;

/**
 * Implementation of a Pen layer.
 */
public class Pen extends BasePen {


    /**
     * Becomes true if the Pen has been set on canvas. ie. there
     * is something to draw.
     */
    protected boolean mSet = false;

    /**
     * Transformer object. Performs the RST transforms
     * on the pen's vertices.
     */
    protected PenTransformer mTransformer;


    /**
     * Bezier controller, controls bezier control
     * points.
     */
    protected BezierController mBezierController;


    /**
     * Controls the vertices.
     */
    protected VertexController mVertexController;


    /**
     * Width of screen
     */
    protected float mScreenWidth;


    /**
     * Height of screen.
     */
    protected float mScreenHeight;


    protected boolean mInteracting = false;

    /**
     * Handler class for undoing rotation transformations.
     */
    protected RotateUndoHandler mRotateUndoHandler = null;

    /**
     * Handler class for undoing scale transformations.
     */
    protected ScaleUndoHandler mScaleUndoHandler = null;

    /**
     * Handler class for undoing translate transformations.
     */
    protected TranslateUndoHandler mTranslateUndoHandler = null;

    /**
     *
     */
    public Pen() {
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


    /**
     * @param strokePaint
     * @param fillPaint
     * @param bezierPoints
     * @param vertexPoints
     */
    protected Pen(Paint strokePaint, Paint fillPaint, ArrayList<PointF> bezierPoints,
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
        for (PointF p : bezierPoints) {
            mBezierVertexList.add(new PointF(p.x, p.y));
        }
        for (PointF p : vertexPoints) {
            mVertexList.add(new PointF(p.x, p.y));
        }
        mVertexController = new VertexController();
        mSet = true;
    }

    @Override
    public void setStrokeColor(int color) {
        super.setStrokeColor(color);


    }


    //
    // BasePen methods
    //

    @Override
    protected IUndo handleDownAction(float x, float y) {
        if (IconManager.instance().getRotationIcon().isInIcon(x, y)) {
            mState = State.ROTATING;

            RotateUndoHandler rh = new RotateUndoHandler();
            rh.cachedRotation = mTransformer.getCurrentRotation();
            return new IUndo(rh);

        } else if (IconManager.instance().getScaleIcon().isInIcon(x, y)) {
            mState = State.SCALING;

            float u = IconManager.instance().getScaleIcon().getXCoord() +
                    IconManager.instance().getScaleIcon().getBitmap().getWidth() * 0.5f;
            float v = IconManager.instance().getScaleIcon().getYCoord() +
                    IconManager.instance().getScaleIcon().getBitmap().getHeight() * 0.5f;
            mScaleUndoHandler = new ScaleUndoHandler();
            mScaleUndoHandler.scaleX = u;
            mScaleUndoHandler.scaleY = v;
            return new IUndo(mScaleUndoHandler);

        } else if (IconManager.instance().getTranslateIcon().isInIcon(x, y)) {
            mState = State.MOVING;

            float u = IconManager.instance().getTranslateIcon().getXCoord() +
                    IconManager.instance().getTranslateIcon().getBitmap().getWidth() * 0.5f;
            float v = IconManager.instance().getTranslateIcon().getYCoord() +
                    IconManager.instance().getTranslateIcon().getBitmap().getHeight() * 0.5f;
            mTranslateUndoHandler = new TranslateUndoHandler();
            mTranslateUndoHandler.xPos = u;
            mTranslateUndoHandler.yPos = v;
            return new IUndo(mTranslateUndoHandler);

        } else {
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

                /*
                 * return an anonymous IUndoHandler here. This handler needs to be able to remove
                 * the recently added point and bezier points, if necessary.
                 */
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
    protected IUndo handleMoveAction(float x, float y) {
        return null;
    }

    @Override
    protected IUndo handleUpAction(float x, float y) {
        return null;
    }


    //
    // ILayer methods
    //

    @Override
    public void setStart() {
        mInteracting = true;
    }

    @Override
    public void setStop() {
        mInteracting = false;
    }

    @Override
    public void draw(Canvas canvas, boolean showTransformBox) {
        if (mSet) {
            mScreenWidth = canvas.getWidth();
            mScreenHeight = canvas.getHeight();
            resetPath();
            if (mVertexList.size() > 1) {
                canvas.drawPath(mPath, mFillPaint);
                canvas.drawPath(mPath, mStrokePaint);

                //only show transform box if this
                // layer is current layer and showtransformbox flag is true.
                if (mInteracting && showTransformBox) {
                    if (mState == State.BEZIER)
                        mBezierController.draw(mVertexList, mBezierVertexList, canvas);
                    else if (mState == State.VERTEX)
                        mVertexController.draw(mVertexList, canvas);
                    //bounding rect and icons
                    mTransformer.draw(canvas);
                }
            }
        }
    }


    @Override
    public IUndo onEvent(MotionEvent event) {
        // first touch, we register, so that the Pen can be drawn on the canvas.
        if (!mSet)
            mSet = true;

        float x, y;
        x = event.getX();
        y = event.getY();
        switch (mState) {
            case UNUSED:
            case IDLE:
                mInteracting = true;
                mState = State.DRAWING;
                break;
            case DRAWING:
                mLastPosition.set(x, y);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return handleDownAction(x, y);
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
        return LayerType.PEN;
    }

    @Override
    public String getTag() {
        return mName;
    }

    @Override
    public void setTag(String tag) {
        mName = tag;
    }

    @Override
    public ILayer copy() {
        Pen pen = new Pen(this.mStrokePaint, this.mFillPaint, mBezierVertexList, mVertexList);
        return pen;
    }

    @Override
    public String getXml() {
        String xml = "<path d='";
        for (int counter = 0; counter < getVertexList().size(); counter++) {
            PointF point = getVertexList().get(counter);
            if (counter == 0)
                xml += " M";
            if (counter != getVertexList().size() - 1) {
                xml += point.x + " " + point.y + " C" + getBezierVertexList().get(2 * counter).x
                        + " " +
                        getBezierVertexList().get(2 * counter).y + " ";
                xml += getBezierVertexList().get(2 * counter + 1).x
                        + " " +
                        getBezierVertexList().get(2 * counter + 1).y + " ";
            } else {
                xml += " " + point.x + ", " + point.y;
            }
        }
        xml += "' ";

        xml += "style='fill: #" + DataUtils.getRGBHex(mFillPaint.getColor()) + ";  fill-opacity: "
                + DataUtils.getAlphaDecimal(DataUtils.getAlpha(mFillPaint.getColor())) + " ; ";
        xml += "stroke: #"
                + DataUtils.getRGBHex(mStrokePaint.getColor()) + ";stroke-width:" + mStrokeThickness + "px; stroke-opacity: "
                + DataUtils.getAlphaDecimal(DataUtils.getAlpha(mStrokePaint.getColor())) + "'";

        xml += "/> ";

        return xml;
    }

    @Override
    public void accept(ILayerVisitor visitor) {
        visitor.visitPen(this);
    }


    /* ------------------------------- Undo Handlers -------------------------------
     * The undo handlers are used to cache some previous state (e.g. translation position etc)
     * that can be easily undone at a later time. The handlers are wrapped in an IUndo
     * object and placed on a stack when being returned from the onMotionEvent(...) method.
     */

    /**
     * Handles undoing a rotation. To undo the rotation it needs to know the angle between
     * the (x,y) pointer position (i.e. user finger on screen) when pointer is moved down,
     * and when it is moved up. To do this, the handler caches the (x,y) position for
     * ON_DOWN actions, and then gets the (x,y) position for ON_UP actions.
     */
    protected class RotateUndoHandler implements IUndoHandler {

        float cachedRotation; /* radians */

        @Override
        public void handleUndo() {
            mTransformer.rotate(cachedRotation - mTransformer.getCurrentRotation());
        }

        @Override
        public void handleRedo() {

        }
    }

    /**
     * Handles undoing scaling. To undo scaling it simply needs to cache the original
     * scale when the  (x,y) pointer position (user finger on screen) was moved down.
     */
    protected class ScaleUndoHandler implements IUndoHandler {

        float scaleX;
        float scaleY;

        @Override
        public void handleUndo() {
            mTransformer.scale(scaleX, scaleY);
        }

        @Override
        public void handleRedo() {

        }
    }

    /**
     * Handles undoing translating. To undo scaling it simply needs to cache the
     * original position of the parent <code>Pen</code> when the
     * (x,y) pointer position (user finger on screen) was moved down.
     */
    protected class TranslateUndoHandler implements IUndoHandler {

        float xPos;
        float yPos;

        @Override
        public void handleUndo() {
            mTransformer.translate(xPos, yPos);
        }

        @Override
        public void handleRedo() {

        }
    }
}
