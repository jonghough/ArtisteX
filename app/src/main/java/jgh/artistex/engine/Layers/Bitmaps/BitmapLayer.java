package jgh.artistex.engine.Layers.Bitmaps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.visitors.ILayerVisitor;
import jgh.artistex.engine.IUndo;
import jgh.artistex.engine.IUndoHandler;
import jgh.artistex.engine.IconManager;
import jgh.artistex.engine.LayerType;
import jgh.artistex.engine.utils.DataUtils;

/**
 * Canvas layer for putting Bitmap images on the screen.
 */
public final class BitmapLayer extends BaseBitmapLayer {

    // debug tag
    private static final String TAG = "BitmapLayer";

    /**
     * Bitmap object.
     */
    private Bitmap mBitmap;


    private float mCacheRealRot;

    /**
     * Path to the bitmap file.
     */
    private String mBitmapPath = null;


    /**
     * Cached position, usually set on pointer down.
     */
    private PointF mCachedPosition;

    private PointF mCachedUnrotatedScale = new PointF(1, 1);

    /**
     * Max of bitmap width, height.
     */
    private float mMaxDimensionSize;

    /**
     * Cached position for scaling bitmaps.
     */
    private PointF mScalePosition;


    /**
     * RTS transform matrix, fro transforming the bitmap.
     */
    private Matrix mMatrix = new Matrix();


    protected RectF mBounds;


    /**
     * True once bitmap appears on the screen. This flag is essentially to distinguish between
     * Bitmaps that have at least been shown and those that were instantiated and unused.
     * (e.g. another layer was selected / instantiated before this layer could be used - in that
     * case, this layer can be discarded.
     */
    private boolean mInitialized = false;


    /**
     * True, while this is currently selected, and being manipulated, false otherwise.
     */
    private boolean mInteracting = true;


    /**
     *
     */
    private boolean mSet = false;


    /**
     * Constructor for <code>BitmapLayer</code> taking Bitmap object, x,y initial coordinates, and
     * references to RTS icons.
     *
     * @param bitmap Bitmap object
     * @param x      x coordinate of initial position.
     * @param y      y coordinate of initial position.
     */
    public BitmapLayer(Bitmap bitmap, float x, float y) {
        super(x, y);

        initialize(bitmap, null);
    }

    /**
     * Copy Constructor
     *
     * @param bitmap
     * @param bitmapPath
     * @param x
     * @param y
     * @param rotation
     * @param scaleX
     * @param scaleY
     */
    private BitmapLayer(Bitmap bitmap, String bitmapPath, float x, float y, float rotation, float scaleX, float scaleY) {
        super(x, y);
        setScaleValueX(scaleX);
        setScaleValueY(scaleY);
        setRotValue(rotation);
        setCachedRotValue(rotation);

        initialize(bitmap, bitmapPath);
    }


    /**
     * @param bitmap
     * @param path
     */
    private void initialize(Bitmap bitmap, String path) {
        mBitmap = bitmap;
        mBitmapPath = path;
        //init fields
        mCachedPosition = new PointF(0, 0);
        mScalePosition = new PointF(0, 0);
        mBounds = new RectF();

        mMode = Mode.TRANSLATE;

        // find the max dimension. Needed for clean scaling and rotating.
        mMaxDimensionSize = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
    }


    /**
     * Gets the  bitmap.
     *
     * @return bitmap
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }


    /**
     * Sets the bitmap object.
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }


    /**
     * Reinitialize the layer.
     */
    private void reInitialize() {
        mInteracting = true;
    }


    /**
     * Set the cached Position
     *
     * @param value <code>PointF</code> position.
     */
    private void setCachedPosition(PointF value) {
        mCachedPosition = value;
    }


    /**
     * Makes a bounding rect for the Bitmap. Should match the RTS transforms of
     * the bitmap and will be drawn on top of the bitmap. Useful for getting the
     * boundary coords of the transformed bitmap.
     */
    private void makeBoundingRectF() {
        float rot = this.getRotValue();
        float sx = getScaleValueX();
        float sy = getScaleValueY();
//        float w = mBitmap.getWidth();
//        float h = mBitmap.getHeight();
        float w = mMaxDimensionSize, h = mMaxDimensionSize;

        mBounds = new RectF(-w / 2, -h / 2, w / 2, h / 2);

        PointF tl = new PointF(mBounds.left, mBounds.top);
        PointF tr = new PointF(mBounds.right, mBounds.top);
        PointF bl = new PointF(mBounds.left, mBounds.bottom);
        PointF br = new PointF(mBounds.right, mBounds.bottom);
        setMatrix();
        scalePoint(tl);
        scalePoint(tr);
        scalePoint(bl);
        scalePoint(br);

        float right = Math.max(Math.max(tl.x, tr.x), Math.max(bl.x, br.x));
        float left = Math.min(Math.min(tl.x, tr.x), Math.min(bl.x, br.x));
        float top = Math.min(Math.min(tl.y, tr.y), Math.min(bl.y, br.y));
        float bottom = Math.max(Math.max(tl.y, tr.y), Math.max(bl.y, br.y));
        mBounds.set(left, top, right, bottom);
        PointF c = this.getCenter();

        mBounds.set(c.x - mBounds.width() / 2, c.y - mBounds.height() / 2, c.x + mBounds.width()
                / 2, c.y + mBounds.height() / 2);
    }


    /**
     * Scales around point.
     *
     * @param point Point to scale and rotate
     */
    private void scalePoint(PointF point) {

        float x = point.x;
        float y = point.y;
        float[] f = new float[]{
                x, y
        };

        mMatrix.mapPoints(f);
        point.x = f[0];
        point.y = f[1];
    }


    /**
     * Sets the matrix to the correct parameters for rotating,
     * scaling and translating (in the correct order)
     */
    private void setMatrix() {
        mMatrix.reset();
        if (getRotValue() != 0) {

            mMatrix.postRotate(getRotValue(), 0 + mBitmap.getWidth() / 2,
                    0 + mBitmap.getHeight() / 2);
        }
        mMatrix.postScale(getScaleValueX(), getScaleValueY());
        mMatrix.postTranslate(getX(), getY()); // translate to correct
    }


    /**
     * Performs RTS transformations on the bitmap and then draws to the canvas.
     * Matrix order is Scale, Translate, Rotate.
     */
    private void transformBitmap() {

        setMatrix();
        setIconCoords();
        makeBoundingRectF();
        return;
    }


    /**
     * Performs the same operation as transformBitmap, but stores the matrix as
     * a MatrixAction for undoing.
     */
    private IUndo setBitmapOnDown() {
        MatrixUndo ma = new MatrixUndo(this);
        ma.x = getX();
        ma.y = getY();
        ma.scaleX = this.getScaleValueX();
        ma.scaleY = this.getScaleValueY();
        ma.rotation = this.getRotValue();
        return new IUndo(ma);

    }


    //
    // Motion event actions.
    //


    /**
     * On Up Motion event. User raises finger from screen action.
     *
     * @param x the x coordinate of the action event
     * @param y the y coordinate of the action event
     * @return <code>IUndo</code> action, to be place on undo stack.
     */
    private IUndo onDown(float x, float y) {
        if (!mInitialized) {

            final float u = getX();
            final float v = getY();

            setCoords(x, y);
            setCachedPosition(new PointF(getX(), getY()));


            makeBoundingRectF();
            float norm = (float) Math.sqrt(((mBounds.bottom - mBounds.centerY())
                    * (mBounds.bottom - mBounds.centerY()) + (mBounds.left - mBounds.centerX())
                    * (mBounds.left - mBounds.centerX())));
            // account for the rotation icon being not at the top center
            float iconAngle = (float) Math.acos(
                    (mBounds.bottom - mBounds.centerY()) / norm);

            mCacheRealRot = (float) (iconAngle * 180f / Math.PI);

            mInitialized = true;
            return new IUndo(new IUndoHandler() {

                float redoX;
                float redoY;

                @Override
                public void handleUndo() {
                    redoX = getX();
                    redoY = getY();
                    setCoords(u, v);
                    setCachedPosition(new PointF(getX(), getY()));
                }

                @Override
                public void handleRedo() {
                    setCoords(redoX, redoY);
                    setCachedPosition(new PointF(getX(), getY()));
                }
            });

        }
        // set the correct mode
        if (mSet) {
            if (IconManager.instance().getRotationIcon().isInIcon(x, y)) {
                setMode(Mode.ROTATE);

                return setBitmapOnDown();
            } else if (IconManager.instance().getScaleIcon().isInIcon(x, y)) {
                if (getScaleValueX() < 0) {
                    mScalePosition.x = mBounds.right;
                } else {
                    mScalePosition.x = mBounds.left;
                }
                if (getScaleValueY() < 0) {
                    mScalePosition.y = mBounds.bottom;
                } else {
                    mScalePosition.y = mBounds.top;

                }
                setMode(Mode.SCALE);
                return setBitmapOnDown();
            } else if (IconManager.instance().getTranslateIcon().isInIcon(x, y)) {
                setMode(Mode.TRANSLATE);
                return setBitmapOnDown();
            } else {
                setMode(Mode.NONE);
            }
        }
        return null;
    }


    /**
     * On Move Motion event. User moves finger on screen action.
     *
     * @param x the x coordinate of the action event
     * @param y the y coordinate of the action event
     */
    private void onMove(float x, float y) {
        if (!mSet)
            return;
        else {
            makeBoundingRectF();
            if (getMode() == Mode.ROTATE) {

                /*
                 * The rotation value needs to be chosen carefully. If the
                 * x-scale value is negative the current rotation value needs to
                 * be subtracted from the cached rotation value. Likewise for
                 * the y-scale value. Also, if the y-scale value is negative we
                 * must subtract 180 from the current rotation value. Otherwise,
                 * the rotation is flipped the wrong way.
                 */
                float rot = getCachedRotValue() + Math.signum(getCenter().x - getX())
                        * Math.signum(getCenter().y - getY()) * getRotationAngle(x, y);

                if (getY() - getCenter().y > 0) {
                    rot = rot - 180;

                }

                float norm = (float) Math.sqrt(((mBounds.bottom - mBounds.centerY())
                        * (mBounds.bottom - mBounds.centerY()) + (mBounds.left - mBounds.centerX())
                        * (mBounds.left - mBounds.centerX())));
                // account for the rotation icon being not at the top center
                float iconAngle = (float) Math.acos(
                        (mBounds.bottom - mBounds.centerY()) / norm);
                iconAngle = (float) (iconAngle * 180 / Math.PI);
                rot -= mCacheRealRot - 180;//iconAngle- 180;

                setRotValue(rot % 360);

                float r = getRotValue();

                // lets rotate the scale factors too.
                float rads = r * (float) Math.PI / 180.0f;
                float COS = (float) Math.cos(rads);
                float SIN = (float) Math.sin(rads);
                float TAN = (float) Math.tan(rads);

                float ox = mCachedUnrotatedScale.x;
                float oy = mCachedUnrotatedScale.y;

                float A = (float) Math.sqrt((ox * ox * COS * COS - oy * oy * SIN * SIN) / (COS * COS - SIN * SIN));
                float B = (float) (Math.sqrt((oy * oy - A * A * SIN * SIN) / (COS * COS)));
                if (TAN != TAN || Math.abs(COS - 0) < 0.001f) {
                    A = oy;
                    B = ox;
                }

                float diff = Math.abs(0 - (rot % 45.0f));

                if (diff < 5 || diff > 40) {
                    return;
                }


            } else if (getMode() == Mode.SCALE) {
                float xs = (x - mScalePosition.x) / mBounds.width();
                float ys = (y - mScalePosition.y) / mBounds.height();

                setScaleValueX(Math.abs(getScaleValueX()) * xs);
                setScaleValueY(Math.abs(getScaleValueY()) * ys);

                if (Math.abs(getRotValue() - 0) < 0.001f) {
                    mCachedUnrotatedScale.x = getScaleValueX();
                    mCachedUnrotatedScale.y = getScaleValueY();
                }

                if (getScaleValueX() < 0) {
                    setX(getX() + (-mBounds.right + mScalePosition.x) / 2);
                } else {
                    setX(getX() + (-mBounds.left + mScalePosition.x) / 2);
                }
                if (getScaleValueY() < 0) {
                    setY(getY() + (-mBounds.bottom + mScalePosition.y) / 2);
                } else {
                    setY(getY() + (-mBounds.top + mScalePosition.y) / 2);
                }
                makeBoundingRectF();
                float norm = (float) Math.sqrt(((mBounds.bottom - mBounds.centerY())
                        * (mBounds.bottom - mBounds.centerY()) + (mBounds.left - mBounds.centerX())
                        * (mBounds.left - mBounds.centerX())));
                // account for the rotation icon being not at the top center
                float iconAngle = (float) Math.acos(
                        (mBounds.bottom - mBounds.centerY()) / norm);

                mCacheRealRot = (float) (iconAngle * 180f / Math.PI);
                return;
            } else if (getMode() == Mode.TRANSLATE) {

                float xc = getX();
                float yc = getY();
                // calc dist from rect topleft/topright
                float distx = getX() - mBounds.left;
                float disty = getY() - mBounds.top;
                if (getScaleValueX() < 0) {
                    distx = getX() - mBounds.right;
                }
                if (getScaleValueY() < 0) {
                    disty = getY() - mBounds.bottom;
                }
                setX(x + distx);
                setY(y + disty);
                float newCachedX = mCachedPosition.x + getX() - xc;
                float newCachedY = mCachedPosition.y + getY() - yc;
                mCachedPosition.set(new PointF(newCachedX, newCachedY));

            } else if (getMode() == Mode.NONE) {
                // empty
            }
        }
    }

    /**
     * On Up Motion event. User raises finger from screen action.
     *
     * @param x the x coordinate of the action event
     * @param y the y coordinate of the action event
     */
    private void onUp(float x, float y) {
        if (getMode() == Mode.ROTATE) {
            setCachedRotValue(getRotValue());
        }
        if (getMode() == Mode.NONE && mSet) {
        }
        mSet = true;
    }


    //
    // Abstract methods from <code>BaseBitmapLayer</code>
    //

    @Override
    protected void setIconCoords() {
        // if not initialized then bitmap has no coords
        if (mBitmap == null || !mInitialized) {
            return;
        }

        if (mBounds != null) {
            float left = mBounds.left, top = mBounds.top, right = mBounds.right, bottom = mBounds.bottom;
            if (getScaleValueX() < 0) {
                left = mBounds.right;
                right = mBounds.left;
            }
            if (getScaleValueY() < 0) {
                top = mBounds.bottom;
                bottom = mBounds.top;
            }
            IconManager.instance().getRotationIcon().setXCoord(left);
            IconManager.instance().getRotationIcon().setYCoord(bottom);

            IconManager.instance().getScaleIcon().setXCoord(right);
            IconManager.instance().getScaleIcon().setYCoord(bottom);

            IconManager.instance().getTranslateIcon().setXCoord(left);
            IconManager.instance().getTranslateIcon().setYCoord(top);

        }
    }

    @Override
    protected PointF getCenter() {
        float x = getX() + mBitmap.getWidth() * getScaleValueX() / 2;
        float y = getY() + mBitmap.getHeight() * getScaleValueY() / 2;
        return new PointF(x, y);
    }


    //
    // Methods from <code>ILayer</code>
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
        canvas.drawBitmap(mBitmap, mMatrix, null);
        if (mSet && mInteracting && showTransformBox) {

            if (mBounds != null) {
                Paint p = new Paint(Color.RED);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(mBounds, p);
            }

            drawIcons(canvas);
        }
    }

    @Override
    public IUndo onEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return onDown(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            onMove(event.getX(), event.getY());
            // return null;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            onUp(event.getX(), event.getY());
            //return null;
        }
        transformBitmap();
        return null;
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.IMAGE;
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
        Bitmap bmp = Bitmap.createBitmap(getBitmap());
        BitmapLayer bml = new BitmapLayer(bmp, mBitmapPath, this.getX(), this.getY(), getRotValue(), getScaleValueX(), getScaleValueY());

        return bml;

    }

    @Override
    public String getXml() {
        // String ext = FileUtils.getImageExtension(mBitmapPath);

        String ext = "png";
        String xml = "";
        String encode = DataUtils.encodeTobase64(mBitmap, Bitmap.CompressFormat.PNG);

        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        float x = getX();
        float y = getY();
        xml = "<g transform=\"translate(" + x + ", " + y + ") scale("
                + getScaleValueX() + ", " + getScaleValueY() + ") rotate(" + getRotValue() + " "
                + w / 2 + " " + h / 2 + ") \"><image x=\"" + 0
                + "\" y=\"" + 0 + "\" width=\"" + w + "\" height=\"" + h
                + "\" xlink:href=\"data:image/" + ext + ";base64, " + encode + " \"/></g>";

        return xml;
    }

    /**
     * Returns the rotation, scale and translation information for the bitmap in a
     * SVG <i>transform</i> string.
     *
     * @return SVG transform data
     */
    /* default */ String wrapXmlWithTransformation() {
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        float x = getX();
        float y = getY();
        String xml = "transform=\"translate(" + x + ", " + y + ") scale("
                + getScaleValueX() + ", " + getScaleValueY() + ") rotate(" + getRotValue() + " "
                + w / 2 + " " + h / 2 + ")\"";

        return xml;
    }

    @Override
    public void accept(ILayerVisitor visitor) {
        visitor.visitBitmap(this);
    }


    //
    // <code>IUndo</code> implementations.
    //


    /**
     *
     */
    private class MatrixUndo implements IUndoHandler {

        /**
         *
         */
        public float x, y;


        /**
         *
         */
        public float scaleX, scaleY;


        /**
         *
         */
        public float rotation;


        /**
         *
         */
        private BitmapLayer mBitmapLayer;


        public MatrixUndo(BitmapLayer bitmapLayer) {
            mBitmapLayer = bitmapLayer;
        }


        @Override
        public void handleUndo() {
            setRotValue(rotation);
            setScaleValueX(scaleX);
            setScaleValueY(scaleY);
            setX(x);
            setY(y);
            transformBitmap();
            if (mBounds != null) {
                makeBoundingRectF();
            }
            setIconCoords();
        }

        @Override
        public void handleRedo() {

        }
    }
}
