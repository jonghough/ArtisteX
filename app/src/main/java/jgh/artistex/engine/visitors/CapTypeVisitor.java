package jgh.artistex.engine.visitors;


import android.graphics.Paint;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.Layers.Bitmaps.BaseBitmapLayer;
import jgh.artistex.engine.Layers.Bitmaps.TextLayer;
import jgh.artistex.engine.Layers.Pens.BasePen;

public class CapTypeVisitor implements ILayerVisitor {

    private Paint.Cap mCap;

    public CapTypeVisitor(Paint.Cap cap){
        mCap = cap;
    }

    @Override
    public void visit(ILayer layer) {

    }

    @Override
    public void visitPen(BasePen basePen) {
        basePen.setCapType(mCap);
    }

    @Override
    public void visitBitmap(BaseBitmapLayer layer) {
        //
    }


    @Override
    public void visitText(TextLayer layer) {
        layer.setCapType(mCap);
    }
}
