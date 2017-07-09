package jgh.artistex.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

import jgh.artistex.engine.utils.DataUtils;
import jgh.artistex.engine.utils.FileUtils;
import jgh.artistex.engine.visitors.CapTypeVisitor;
import jgh.artistex.engine.visitors.ColorSetVisitor;
import jgh.artistex.engine.visitors.ILayerVisitor;
import jgh.artistex.engine.visitors.JoinTypeVisitor;
import jgh.artistex.engine.visitors.StrokeThicknessVisitor;

/**
 * Drawing engine. Singleton object containing the draw and motion event calls to
 * all the <code>ILayer</code> objects. Calls to manipulate / edit layers go through
 * <code>DrawingEngine</code>
 */
public class DrawingEngine {

    /**
     * the instance of the Drawing Engine
     */
    private static DrawingEngine sEngine;

    /**
     * List of the <code>ILayer</code> layers in order to be
     * drawn to the canvas. It's not a stack, because insert and removal
     * of arbitrary elements is necessary... so ... rename.
     */
    private ArrayList<ILayer> mDrawingStack;

    /**
     * Undo item stack. This *is* a stack.
     */
    private Stack<IUndo> mUndoStack;

    /**
     * The currently edited <code>ILayer</code>
     */
    private ILayer mCurrentLayer;

    private RectF mBackgroundRect;

    private float mScreenWidth;
    private float mScreenHeight;



    /**
     * private constructor
     */
    private DrawingEngine() {

        mDrawingStack = new ArrayList<>();
        mUndoStack = new Stack<>();
        // do not instantiate a layer yet. Needs user selection.
        mCurrentLayer = null;
        mBackgroundRect = new RectF();
    }


    /**
     * Returns the single instance of the <code>DrawingEngine</code> class.
     *
     * @return instance.
     */
    public static DrawingEngine Instance() {
        if (sEngine == null)
            sEngine = new DrawingEngine();
        return sEngine;
    }


    /**
     * Sets the screen dimensions.
     *
     * @param width  screen width
     * @param height screen height
     */
    public void setScreenDimensions(float width, float height) {
        mScreenWidth = width;
        mScreenHeight = height;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getLayerNameList(){
        ArrayList<String> layerNames = new ArrayList<>();
        for(ILayer layer : mDrawingStack){
            layerNames.add(layer.getTag());
        }
        if(mCurrentLayer != null){
            layerNames.add(mCurrentLayer.getTag());
        }
        return layerNames;
    }

    /**
     * Reset. All layers and related objects are destroyed, and the
     * drawing engine is reset.
     */
    public static void killEngine() {
        sEngine = null;
    }

    /**
     *
     * @param showIt
     */
    public void setShowTransformBox(boolean showIt){
        GlobalValues.showTransformBox = showIt;
        IconManager.instance().setShowingIcons(showIt);
    }

    /**
     * Gets the state of the <i>showing transform box</i> flag.
     * @return true if showing transform box flag is true, false otherwise.
     */
    public boolean getShowTransformBox(){
        return GlobalValues.showTransformBox;
    }

    public int getBackgroundColor(){
        return GlobalValues.backgroundColor;
    }

    /**
     * Sets the background color.
     * @param color the color int. (ARGB)
     */
    public void setBackgroundColor(int color){
        final int currentColor = GlobalValues.backgroundColor;
        GlobalValues.backgroundColor = color;
        IUndoHandler undoHandler = new IUndoHandler() {
            @Override
            public void handleUndo() {
                GlobalValues.backgroundColor = currentColor;

            }

            @Override
            public void handleRedo() {

            }
        };

        mUndoStack.push(new IUndo(undoHandler));
    }

    /**
     * Gets the current fill color global variable.
     * @return
     */
    public int getFillColor(){
        return GlobalValues.fillColor;
    }




    /**
     * Sets the fill color global variable. If there is a current
     * <code>ILayer</code> then the fill color of the <code>ILayer</code>
     * will be changed.
     * @param color fill color to set.
     */
    public void setFillColor(int color){
        final int currentColor = GlobalValues.fillColor;
        GlobalValues.fillColor = color;
        ColorSetVisitor csv = new ColorSetVisitor(color,1);
        if(mCurrentLayer != null)
            mCurrentLayer.accept(csv);
        final ILayer layer = mCurrentLayer;

        IUndoHandler handler = new IUndoHandler() {
            @Override
            public void handleUndo() {
                GlobalValues.fillColor = currentColor;
                if(layer != null) {
                    ColorSetVisitor csv2 = new ColorSetVisitor(currentColor, 1);
                    layer.accept(csv2);
                }
            }

            @Override
            public void handleRedo() {

            }
        };
        mUndoStack.push(new IUndo(handler));
    }



    /**
     *
     * @return
     */
    public int getStrokeColor(){
        return GlobalValues.strokeColor;
    }

    /**
     * Sets the stroke color global variable. If there is a current
     * <code>ILayer</code> then the fill color of the <code>ILayer</code>
     * will be changed.
     * @param color stroke color to set.
     */
    public void setStrokeColor(int color){
        final int currentColor = GlobalValues.strokeColor;
        GlobalValues.strokeColor = color;
        ColorSetVisitor csv = new ColorSetVisitor(color, 0);
        if(mCurrentLayer != null)
            mCurrentLayer.accept(csv);
        final ILayer layer = mCurrentLayer;

        IUndoHandler handler = new IUndoHandler() {
            @Override
            public void handleUndo() {
                GlobalValues.fillColor = currentColor;
                if(layer != null) {
                    ColorSetVisitor csv2 = new ColorSetVisitor(currentColor, 0);
                    layer.accept(csv2);
                }
            }

            @Override
            public void handleRedo() {

            }
        };
        mUndoStack.push(new IUndo(handler));
    }

    public void setStrokeThickness(int thickness){
        final int currentThickness = GlobalValues.strokeThickness;
        GlobalValues.strokeThickness = thickness;
        StrokeThicknessVisitor stv = new StrokeThicknessVisitor(thickness);
        if(mCurrentLayer != null)
            mCurrentLayer.accept(stv);
        final ILayer layer = mCurrentLayer;

        IUndoHandler handler = new IUndoHandler() {
            @Override
            public void handleUndo() {
                GlobalValues.strokeThickness = currentThickness;
                if(layer != null) {
                    StrokeThicknessVisitor stv2 = new StrokeThicknessVisitor(currentThickness);
                    layer.accept(stv2);
                }
            }

            @Override
            public void handleRedo() {

            }
        };
        mUndoStack.push(new IUndo(handler));
    }

    /**
     *
     * @return
     */
    public Paint.Cap getCapType(){
        return GlobalValues.capType;
    }

    /**
     * Sets the global <i>Stroke Cap</i> type, and if there is a currently
     * active layer will set this layer's stroke cap type to the same value,
     * if applicable (e.g. not applicable for <code>BitmapLayer</code>s.
     * @param cap cap type
     */
    public void setCapType(Paint.Cap cap){
        final Paint.Cap cacheCap = GlobalValues.capType;
        GlobalValues.capType = cap;

        CapTypeVisitor ctv = new CapTypeVisitor(cap);

        if(mCurrentLayer != null)
            mCurrentLayer.accept(ctv);
        final ILayer layer = mCurrentLayer;

        IUndoHandler handler = new IUndoHandler() {
            @Override
            public void handleUndo() {
                GlobalValues.capType = cacheCap;
                if(layer != null) {
                    CapTypeVisitor ctv2 = new CapTypeVisitor(cacheCap);
                    layer.accept(ctv2);
                }
            }

            @Override
            public void handleRedo() {

            }
        };
        mUndoStack.push(new IUndo(handler));
    }

    /**
     *
     * @return
     */
    public Paint.Join getJoinType(){
        return GlobalValues.joinType;
    }

    /**
     * Sets the global <i>Stroke Join</i> type, and if there is a currently
     * active layer will set this layer's stroke join type to the same value,
     * if applicable (e.g. not applicable for <code>BitmapLayer</code>s.
     * @param join join type
     */
    public void setJoinType(Paint.Join join){
        final Paint.Join cacheJoin = GlobalValues.joinType;
        GlobalValues.joinType = join;

        JoinTypeVisitor jtv = new JoinTypeVisitor(join);

        if(mCurrentLayer != null)
            mCurrentLayer.accept(jtv);
        final ILayer layer = mCurrentLayer;

        IUndoHandler handler = new IUndoHandler() {
            @Override
            public void handleUndo() {
                GlobalValues.joinType = cacheJoin;
                if(layer != null) {
                    JoinTypeVisitor jtv2 = new JoinTypeVisitor(cacheJoin);
                    layer.accept(jtv2);
                }
            }

            @Override
            public void handleRedo() {

            }
        };
        mUndoStack.push(new IUndo(handler));
    }

    /**
     * Allows a <code>ILayerVisitor</code> implementaiton to visit, and perform
     * some action onthe current layer object. Returns false if the current layer is null,
     * otherwise returns true after the visitor visits the layer.
     *
     * @param visitor <code>ILayerVisitor</code> implementation
     * @return true if visited, false if current layer is null.
     */
    public boolean visitLayer(ILayerVisitor visitor) {
        if (mCurrentLayer == null)
            return false;
        else {
            mCurrentLayer.accept(visitor);
            return true;
        }
    }

    /**
     * Copies the current layer if there is a current layer.
     * @return True if a new layer was created, false otherwsie.
     */
    public boolean copy(){
        if(mCurrentLayer != null){
            ILayer nextLayer = mCurrentLayer.copy();
            if(nextLayer != null){
                final ILayer originalLayer = mCurrentLayer;
                mCurrentLayer.setStop();
                mDrawingStack.add(mCurrentLayer);
                mCurrentLayer = nextLayer;
                mCurrentLayer.setTag(mCurrentLayer.getLayerType().toString()+"_"+createRandomString());
                IUndoHandler handler = new IUndoHandler() {

                    @Override
                    public void handleUndo() {
                        // we put back the original layer.
                        // the copy layer reference is lost, so will
                        // be garbage collected.
                        mDrawingStack.remove(mDrawingStack.size() - 1);
                        mCurrentLayer = originalLayer;
                        mCurrentLayer.setStart();
                    }

                    @Override
                    public void handleRedo() {

                    }
                };

                mUndoStack.push(new IUndo(handler));

                nextLayer.setStart();
                return true;
            }
        }
        return false;
    }

    /**
     * Draws all <code>ILayer</code> Layers on the given canvas
     *
     * @param canvas Canvas
     */
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(GlobalValues.backgroundColor);
        canvas.drawRect(0,0,mScreenWidth, mScreenHeight, p);
        for (ILayer layer : mDrawingStack) {
            layer.draw(canvas, true);
        }
        if (mCurrentLayer != null) {
            mCurrentLayer.draw(canvas, GlobalValues.showTransformBox);
        }
    }


    /**
     *
     * @param event
     */
    public void onMotionEvent(MotionEvent event) {
        if (mCurrentLayer != null) {
            IUndo undo = mCurrentLayer.onEvent(event);
            if (undo != null)
                mUndoStack.push(undo);
        }
    }


    /**
     * Sets the top layer. THe current top layer will be put into the drawing stack.
     *
     * @param layer
     */
    public void setLayer(ILayer layer) {
        if (layer == null)
            return;
        else {
            if (mCurrentLayer != null) {
                if (mDrawingStack == null)
                    mDrawingStack = new ArrayList<>();
                mCurrentLayer.setStop();
                mDrawingStack.add(mCurrentLayer);
            }
            mCurrentLayer = layer;
            new ColorSetVisitor(GlobalValues.strokeColor, 0).visit(layer);
            new ColorSetVisitor(GlobalValues.fillColor, 1).visit(layer);
            new StrokeThicknessVisitor(GlobalValues.strokeThickness).visit(layer);
            mCurrentLayer.setTag(mCurrentLayer.getLayerType().toString()+"_"+createRandomString());
            mCurrentLayer.setStart();
            DestroyHandler destroyHandler = new DestroyHandler(mCurrentLayer);
            IUndo undo = new IUndo(destroyHandler);
            mUndoStack.push(undo);
        }
    }

    /**
     * Handles destruction of the most recently used <code>ILayer</code>
     * layers.
     */
    private class DestroyHandler implements IUndoHandler {

        private ILayer mLayerToDestroy;

        public DestroyHandler(ILayer layer) {
            mLayerToDestroy = layer;
        }

        @Override
        public void handleUndo() {
            if (mDrawingStack.contains(mLayerToDestroy)) {
                mDrawingStack.remove(mLayerToDestroy);
            }

            if (mDrawingStack.size() > 0) {
                mCurrentLayer = mDrawingStack.get(mDrawingStack.size() - 1);
                mCurrentLayer.setStart();
            } else
                mCurrentLayer = null;
        }

        @Override
        public void handleRedo() {

        }
    }


    /**
     * Undo the last action.
     */
    public void undo() {
        if(!mUndoStack.isEmpty()) {
            IUndo undo = mUndoStack.pop();
            undo.undo();
        }
    }

    public void delete(int index) throws IndexOutOfBoundsException{
        final int i = index;
        final ILayer layer;
        final boolean isInDrawingStack;
        if(index < mDrawingStack.size()) {
            isInDrawingStack = true;
            try {
                layer = mDrawingStack.remove(index);
            } catch (IndexOutOfBoundsException e) {
                throw e;
            }
        }
        else{
            isInDrawingStack = false;
            layer = mCurrentLayer;
            if(mDrawingStack.size() > 0) {
                ILayer poppedLayer = mDrawingStack.remove(mDrawingStack.size() - 1);
                mCurrentLayer = poppedLayer;
                mCurrentLayer.setStart();
            }
            else{
                mCurrentLayer = null;
            }
        }

        IUndoHandler undoHandler = new IUndoHandler() {
            boolean putInStack = isInDrawingStack;
            @Override
            public void handleUndo() {

                if(putInStack)
                    mDrawingStack.add(i, layer);
                else{
                    if(mCurrentLayer != null){
                        mDrawingStack.add(mCurrentLayer);
                        mCurrentLayer.setStop();
                    }
                    mCurrentLayer = layer;
                    mCurrentLayer.setStart();
                }
            }

            @Override
            public void handleRedo() {

            }
        };

        mUndoStack.push(new IUndo(undoHandler));
    }




    /**
         * Saves the contents of the screen to a bitmap file, of the given format. All <code>ILayer</code>
         * objects inside the screen bounds will be drawn to the bitmap file.
         *
         * @param width  screen width
         * @param height screen height
         * @param format the format, PNG or JPEG
         * @throws FileNotFoundException
         */
    private void saveScreenAsImage(int width, int height, Bitmap.CompressFormat format)
            throws FileNotFoundException {

        // must nullify the tool;
        if (mCurrentLayer != null) {
            mCurrentLayer.setStop();
            mDrawingStack.add(mCurrentLayer);
            mCurrentLayer = null;
        }

        Bitmap output = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        this.draw(canvas);

        File file = new File(FileUtils.getImagesDirectory()+FileUtils.getUniqueTempFilename(format.name()));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            //TODO
        }

        output.compress(format, 100, fos);

        try{
            if (fos != null){
                fos.close();
            }
        }catch(IOException e){
            //TODO
        }
    }

    /**
     * Saves the Screen and layers on the screen as a bitmap.
     * @param width screen width (pixels)
     * @param height screen height (pixels)
     * @param format Compression format
     */
    public void saveScreenBitmap(int width, int height, Bitmap.CompressFormat format){
        final int w = width;
        final int h = height;
        final Bitmap.CompressFormat bitmapFormat = format;
        IEngineTask task = new IEngineTask() {
            @Override
            public void setup() {

            }

            @Override
            public boolean doTask() {
                try {
                    saveScreenAsImage(w, h, bitmapFormat);
                } catch (FileNotFoundException e) {
                    Log.e("DrawingEngine",e.getMessage());
                    return false;
                }
                return true;
            }

            @Override
            public void onResult(boolean result) {
                //whatever. (Toast message would be good).
            }
        };
        new BackgroundTask(task).execute();
    }


    /**
     * Saves the SVG data of each layer into one SVG file.
     * @param width canvas width
     * @param height canvas height
     */
    public void saveSvgData(int width, int height){
        final int w = width;
        final int h = height;
        IEngineTask task = new IEngineTask() {
            @Override
            public void setup() {

            }

            @Override
            public boolean doTask() {
                try {
                    saveAllLayersSvg(w, h);
                } catch (IOException e) {
                    Log.e("DrawingEngine",e.getMessage());
                    return false;
                }
                return true;
            }

            @Override
            public void onResult(boolean result) {
                //whatever. (Toast message would be good).
            }
        };
        new BackgroundTask(task).execute();
    }

    /**
     * Saves all layers and background layer, if it exists, as a single SVG
     * file. Each layer is serialized to a single SVG entity.
     *
     * @param width  width of canvas or SVG width
     * @param height SVG height
     * @throws IOException
     */
    private void saveAllLayersSvg(int width, int height) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append(FileUtils.SVG_HEADER);
        sb.append(" "
                + "<svg width=\""
                + width
                + "\" height=\""
                + height
                + "  style='fill:#"
                + DataUtils.getRGBHex(0xFFFFFF)
                + "'"
                + "\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");// do
        // need
        sb.append("<rect width=\""
                + width
                + "\" height=\""
                + height
                + " \" style='fill:#" + DataUtils.getRGBHex(0xFFFFFF) + ";"

                + " fill-opacity:"
                + DataUtils.getAlphaDecimal(DataUtils.getAlpha(0xFFFFFF))
                + "' />");

        for (ILayer layer : mDrawingStack) {
            String xml = layer.getXml();
            if (xml != null)
                sb.append(xml);

        }

        sb.append(FileUtils.SVG_FOOTER);

        File file = new File(FileUtils.getImagesDirectory() + FileUtils.getSvgFilename());

        try {
            file.createNewFile();
        } catch (IOException e1) {
            throw e1;
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e2) {
            throw e2;
        }
        try {
            writer.append(sb.toString());
        } catch (IOException e) {
            writer.close();
            throw e;
        }
        try {
            writer.flush();
        } catch (IOException e1) {
            writer.close();
            throw e1;
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw e;
        }
    }


    private String createRandomString(){
        return UUID.randomUUID().toString().substring(0,5);
    }

    /**
     * Interface for running tasks in the background.
     */
    /* default */ interface IEngineTask{

        /**
         * Initial setup to perform the task.
         */
        void setup();

        /**
         * Perform the task.
         * @return True if the operation was successful, false otherwise.
         */
        boolean doTask();

        /**
         * Operaiton to perform after task is completed.
         * @param result True if success, false if background task failed.
         */
        void onResult(boolean result);
    }
}
