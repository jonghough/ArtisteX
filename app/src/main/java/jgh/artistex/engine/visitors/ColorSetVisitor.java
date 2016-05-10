package jgh.artistex.engine.visitors;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.Layers.Bitmaps.BaseBitmapLayer;
import jgh.artistex.engine.Layers.Bitmaps.TextLayer;
import jgh.artistex.engine.Layers.Pens.BasePen;


/**
 * Sets the color of <code>ILayer</code> implementations.
 */
public class ColorSetVisitor implements ILayerVisitor {

    private int mColor;
    private int mType; // 0 = stroke, 1 = fill


    public ColorSetVisitor(int color, int type) {
        mColor = color;
        mType = type;
    }


    @Override
    public void visit(ILayer layer) {
        layer.accept(this);
    }

    @Override
    public void visitPen(BasePen basePen) {
        if(mType == 0)
            basePen.setStrokeColor(mColor);
        else
            basePen.setFillColor(mColor);
    }


    @Override
    public void visitBitmap(BaseBitmapLayer layer) {
        //none
    }


    @Override
    public void visitText(TextLayer layer) {
        layer.setColor(mColor);
    }
}
