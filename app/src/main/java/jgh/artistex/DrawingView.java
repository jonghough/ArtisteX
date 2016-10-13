package jgh.artistex;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import jgh.artistex.engine.DrawingEngine;
import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.visitors.ILayerVisitor;

/**
 *
 */
public class DrawingView extends View {



    /**
     * Screen dimensions.
     */
    private int[] mDimensions = new int[2];


    /**
     * Constructor 1
     *
     * @param context
     */
    public DrawingView(Context context) {
        super(context);
        initialize();
    }


    /**
     * Constructor 2
     *
     * @param context
     * @param attrs
     */
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }


    /**
     * Constructor 3
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    /**
     * Constructor 4
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }


    /**
     * Initialize the <code>DrawingView</code>.
     */
    private void initialize() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mDimensions[1] = metrics.heightPixels;
        mDimensions[0] = metrics.widthPixels;
        DrawingEngine.Instance().setScreenDimensions(metrics.widthPixels, metrics.heightPixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DrawingEngine.Instance().draw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        DrawingEngine.Instance().onMotionEvent(event);
        invalidate();
        return true;

    }


    /**
     * Sets the current layer to be shown and manipulated by the user.
     *
     * @param layer The next layer object.
     */
    public void setLayer(ILayer layer) {
        DrawingEngine.Instance().setLayer(layer);
        invalidate();
    }


    /**
     * Set the <i>show transform box</i> flag. If false, RTS transforms
     * are disabled and the RTS icons and bounding box are not shown for the
     * currently used layer.
     * @param showIt
     */
    public void setShowTransformBox(boolean showIt){
        DrawingEngine.Instance().setShowTransformBox(showIt);
        invalidate();
    }

    /**
     * Returns the <i>show transform box</i> flag.
     * @return
     */
    public boolean getShowTransformBox(){
        return DrawingEngine.Instance().getShowTransformBox();
    }


    /**
     * Visits the currently active layer, if one exists.
     *
     * @param visitor <code>ILayerVisitor</code> implementation.
     */
    public void visitLayer(ILayerVisitor visitor) {
        DrawingEngine.Instance().visitLayer(visitor);
        invalidate();
    }

    public void copy(){
        DrawingEngine.Instance().copy();
    }

    /**
     * Undo the most recent action.
     */
    public void undo() {
        DrawingEngine.Instance().undo();
        invalidate();
    }

    public void saveSvgData(){
        DrawingEngine.Instance().saveSvgData(mDimensions[0], mDimensions[1]);
    }

    public void saveBitmapData(){
        DrawingEngine.Instance().saveScreenBitmap(mDimensions[0], mDimensions[1], Bitmap.CompressFormat.PNG);
    }


    public int getFillColor(){
        return DrawingEngine.Instance().getFillColor();
    }

    public void setFillColor(int color){
        DrawingEngine.Instance().setFillColor(color);
    }

    public int getStrokeColor(){
        return DrawingEngine.Instance().getStrokeColor();
    }

    public void setStrokeColor(int color){
        DrawingEngine.Instance().setStrokeColor(color);
    }

    public Paint.Cap getCapType(){

        return DrawingEngine.Instance().getCapType();
    }

    public void setCapType(Paint.Cap cap){
        DrawingEngine.Instance().setCapType(cap);
    }

    public Paint.Join getJoinType(){
        return DrawingEngine.Instance().getJoinType();
    }

    public void setJoinType(Paint.Join join){
        DrawingEngine.Instance().setJoinType(join);
    }

    public int getBackgroundColor(){
        return DrawingEngine.Instance().getBackgroundColor();
    }

    public void delete(int index){ DrawingEngine.Instance().delete(index);invalidate();}

    public void setBackgroundColor(int color){
        DrawingEngine.Instance().setBackgroundColor(color);
    }

    public void setStrokeThickness(int thickness){
        DrawingEngine.Instance().setStrokeThickness(thickness);
    }

    public ArrayList<String> getLayerNameList(){
        return DrawingEngine.Instance().getLayerNameList();
    }

    public void clearCanvas(){
        DrawingEngine.killEngine();
        invalidate();
    }
}
