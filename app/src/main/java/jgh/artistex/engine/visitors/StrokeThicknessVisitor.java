package jgh.artistex.engine.visitors;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.Layers.Bitmaps.BaseBitmapLayer;
import jgh.artistex.engine.Layers.Bitmaps.TextLayer;
import jgh.artistex.engine.Layers.Pens.BasePen;

/**
 * Visitor class, for setting stroke thickness for the current layer of
 * the <code>Drawing Engine</code>, whatever that means for the
 * given layer type.
 */
public class StrokeThicknessVisitor implements ILayerVisitor {

    private int mThickness;

    public StrokeThicknessVisitor(int thickness){
        mThickness = thickness;
    }

    @Override
    public void visit(ILayer layer) {
        layer.accept(this);
    }

    @Override
    public void visitPen(BasePen basePen) {
        basePen.setStrokeThickness(mThickness);
    }

    @Override
    public void visitBitmap(BaseBitmapLayer layer) {
        //meaningless
    }


    @Override
    public void visitText(TextLayer layer) {
        //meaningless
    }
}
