package jgh.artistex.engine;

import android.graphics.Canvas;
import android.view.MotionEvent;

import jgh.artistex.engine.visitors.ILayerVisitor;

/**
 * Interface for layers. A layer is an object that
 * can be drawn on a canvas, and manipulated by the user and MotionEvents.
 */
public interface ILayer {




    /**
     * Signals the <code>ILayer</code> implementation to start being manipulated by the user
     * This indicates the lyaer has been set as the currently manipulated layer.
     */
    void setStart();


    /**
     * Signals the <code>ILayer</code> implementation to stop being manipulated by the user.
     * This indicates that another <code>ILayer</code> implementation is set as the currently
     * manipulated layer.
     */
    void setStop();


    /**
     * Draw the Layer on the given canvas.
     *
     * @param canvas drawing canvas
     */
    void draw(Canvas canvas, boolean showTransformBox);


    /**
     * Applies a motion event to the layer.
     * Effect of the motion event is left to
     * implementation.
     *
     * @param event motion event
     * @return An <code>IUndo</code> object, making the given effect undoable.
     */
    IUndo onEvent(MotionEvent event);


    /**
     * Returns the <code>LayerType</code> of the Layer.
     *
     * @return layer type enum
     */
    LayerType getLayerType();


    /**
     * Returns a string tag, to represent this Layer type or uniquely identify this
     * Layer.
     *
     * @return string tag
     */
    String getTag();


    /**
     * Sets the Layer tag to identify this layer or layer type.
     *
     * @param tag
     */
    void setTag(String tag);


    /**
     * Makes a duplicate copy of this layer.
     *
     * @return duplicate copy.
     */
    ILayer copy();


    /**
     * Serializes the layer into an SVG (XML) formatted string, for exporting.
     *
     * @return SVG string represtining this layer.
     */
    String getXml();


    /**
     * Accepts a visit by the <code>ILayerVisitor</code> implementation.
     *
     * @param visitor Visitor object.
     */
    void accept(ILayerVisitor visitor);


}
