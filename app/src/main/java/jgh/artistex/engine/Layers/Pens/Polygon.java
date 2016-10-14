package jgh.artistex.engine.Layers.Pens;


import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.IUndo;
import jgh.artistex.engine.IconManager;
import jgh.artistex.engine.LayerType;
import jgh.artistex.engine.utils.DataUtils;

/**
 * Class for <i>Polygon</i> Layers. Polygon layers are closed <code>Path</code>s and
 * have a fixed number of vertices and control points, distinguishing them
 * from <code>BasePen</code> types. Vertices, and control points can be manipulated, and the
 * Polygon itself can be transformed under standard affine transforms (rotate, scale, translate).
 */
public class Polygon extends Pen {


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
     * Constructor. Takes an array of vertices, which is implicitly elementwise immutable.
     * The vertixe list is copied into the internal list and its contents cannot be
     * modified (aside from using the event functions.)
     *
     * @param vertices
     */
    public Polygon(ArrayList<PointF> vertices) {
        super();
        mVertexList = new ArrayList<>(vertices);
        setControlPoints();
    }

    /**
     * @param strokePaint
     * @param fillPaint
     * @param bezierPoints
     * @param vertexPoints
     */
    protected Polygon(Paint strokePaint, Paint fillPaint, ArrayList<PointF> bezierPoints,
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

    private void setControlPoints() {
        mBezierVertexList = new ArrayList<>();
        for (int i = 0; i < mVertexList.size()-1; i++) {

            PointF p = mVertexList.get(i);
            PointF q = mVertexList.get((i + 1) % mVertexList.size());
            PointF b1 = new PointF(0.6667f * p.x + 0.3333f * q.x, 0.6667f * p.y + 0.3333f * q.y);

            PointF b2 = new PointF(0.6667f * q.x + 0.3333f * p.x, 0.6667f * q.y + 0.3333f * p.y);
            mBezierVertexList.add(b1);
            mBezierVertexList.add(b2);
        }

    }


    @Override
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
        mPath.close();
    }


    @Override
    protected IUndo handleDownAction(float x, float y) {
        if (IconManager.instance().getRotationIcon().isInIcon(x, y)) {
            mState = State.ROTATING;

            mRotateUndoHandler = new RotateUndoHandler();
            mRotateUndoHandler.cachedRotation = mTransformer.getCurrentRotation();
            RotateUndoHandler rh = new RotateUndoHandler();
            rh.cachedRotation = mTransformer.getCurrentRotation();
            return new IUndo(rh);

        } else if (IconManager.instance().getScaleIcon().isInIcon(x, y)) {
            mState = State.SCALING;

            float u = IconManager.instance().getScaleIcon().getXCoord() + IconManager.instance().getScaleIcon().getBitmap().getWidth() * 0.5f;
            float v = IconManager.instance().getScaleIcon().getYCoord() + IconManager.instance().getScaleIcon().getBitmap().getHeight() * 0.5f;
            mScaleUndoHandler = new ScaleUndoHandler();
            mScaleUndoHandler.scaleX = u;
            mScaleUndoHandler.scaleY = v;
            return new IUndo(mScaleUndoHandler);
        } else if (IconManager.instance().getTranslateIcon().isInIcon(x, y)) {
            mState = State.MOVING;

            mTranslateUndoHandler = new TranslateUndoHandler();
            float u = IconManager.instance().getTranslateIcon().getXCoord() + IconManager.instance().getTranslateIcon().getBitmap().getWidth() * 0.5f;
            float v = IconManager.instance().getTranslateIcon().getYCoord() + IconManager.instance().getTranslateIcon().getBitmap().getHeight() * 0.5f;
            mTranslateUndoHandler.xPos = u;
            mTranslateUndoHandler.yPos = v;
            return new IUndo(mTranslateUndoHandler);
        }
        return null;
    }

    @Override
    public IUndo onEvent(MotionEvent event) {

        float x, y;
        x = event.getX();
        y = event.getY();

        if (!mSet) {
            mSet = true;
            mTransformer.translate(x, y);
        }

        switch (mState) {
            case UNUSED:
            case IDLE:
                mState = State.DRAWING;
                break;
            case DRAWING:
                /* drawing - i.e. adding points to the vertex list - is prohibited.*/
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
                mBezierController.onMotionEvent(event, mBezierVertexList);
                break;
            case VERTEX:
                mVertexController.onMotionEvent(event, mVertexList);
                break;
            default:
                break;
        }
        return null;
    }


    @Override
    public ILayer copy() {
        Polygon pen = new Polygon(this.mStrokePaint, this.mFillPaint, mBezierVertexList, mVertexList);
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
                PointF startP = getVertexList().get(0);
                xml += " " + point.x + " " + point.y + " Q"
                        + getBezierVertexList().get(counter).x + " " +
                        getBezierVertexList().get(counter).y + " " + startP.x + " " + startP.y + " Z ";
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
    public LayerType getLayerType() {
        return LayerType.SHAPE;
    }
}
