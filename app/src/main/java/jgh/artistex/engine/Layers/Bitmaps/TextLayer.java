package jgh.artistex.engine.Layers.Bitmaps;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.visitors.ILayerVisitor;
import jgh.artistex.engine.IUndo;
import jgh.artistex.engine.LayerType;
import jgh.artistex.engine.utils.DataUtils;

/**
 * An implementation of <code>ILayer</code> for displaying
 * text on the canvas.
 */
public class TextLayer implements ILayer {


    /**
     * Unique name of this <code>ILayer</code>.
     */
    protected String mName;
    private BitmapLayer mBitmapLayer;
    private String mText;
    private int mColor;
    private int mSize;
    private Typeface mTypeface;
    private Paint mPaint;
    public TextLayer(String text, int color, int size, Typeface typeface) {
        mText = text;
        mColor = color;
        mSize = size;
        mTypeface = typeface;
        initializePaint();
        initBitmap();
    }

    public TextLayer(TextLayer original){
        mText = original.getText();
        mPaint = new Paint(original.getPaint());
        mColor = mPaint.getColor();
        mSize = (int)mPaint.getTextSize();
        mTypeface = mPaint.getTypeface();
        mBitmapLayer = (BitmapLayer)original.getBitmapLayer().copy();
    }

    private void initializePaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setTextSize(mSize);
        mPaint.setTypeface(mTypeface);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(70);
    }

    /* default */ String getText(){
        return mText;
    }

    /* default */ Paint getPaint(){
        return mPaint;
    }

    /* default */ BitmapLayer getBitmapLayer(){
        return mBitmapLayer;
    }

    /**
     * @param color
     * @return
     */
    public IUndo setColor(int color) {
        ColorUndo ca = new ColorUndo();
        ca.preColor = this.mColor;
        ca.postColor = color;
        this.mColor = color;

        if (mPaint != null) {
            mPaint.setColor(mColor);
            initBitmap();
        }
        return new IUndo(ca);
    }


    /**
     * Sets the color of the text.
     *
     * @param color text color.
     */
    /*default*/ void setColorNoUndo(int color) {
        this.setColor(color);
        initBitmap();
    }


    private void initBitmap(){

        float w =  mPaint.measureText(mText);
        float h = mPaint.getTextSize();
        w = mPaint.measureText(mText);
        h = mPaint.getTextSize();
        Bitmap output = Bitmap.createBitmap((int)w, (int)h ,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        canvas.drawText(mText, 0, h - 20, mPaint);
        if(mBitmapLayer == null)
            mBitmapLayer = new BitmapLayer(output,0,0);
        else mBitmapLayer.setBitmap(output);
    }

    public void setCapType(Paint.Cap cap){
        mPaint.setStrokeCap(cap);
    }

    public void setJoinType(Paint.Join join){
        mPaint.setStrokeJoin(join);
    }

    @Override
    public void setStart() {
        mBitmapLayer.setStart();
    }

    @Override
    public void setStop() {
        mBitmapLayer.setStop();
    }



    @Override
    public void draw(Canvas canvas, boolean showTransformBox) {
        mBitmapLayer.draw(canvas, showTransformBox);
    }

    @Override
    public IUndo onEvent(MotionEvent event) {
        return mBitmapLayer.onEvent(event);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.TEXT;
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
        return new TextLayer(this);
    }

    @Override
    public String getXml() {
        if (mText == null || mText.isEmpty())
            return "";

        String xml = "<text "+mBitmapLayer.wrapXmlWithTransformation()+" x='" + 70 * mBitmapLayer.getScaleValueX()+ "' y='" + 70 *mBitmapLayer.getScaleValueY() + "' style=\"font-size  :"
                + 70 + ";" + "fill : #" + DataUtils.getRGBHex(this.mColor) + ";"
                + "\" >" + this.mText + "</text>";

        return xml;
    }

    @Override
    public void accept(ILayerVisitor visitor) {
        visitor.visitText(this);
    }
}
