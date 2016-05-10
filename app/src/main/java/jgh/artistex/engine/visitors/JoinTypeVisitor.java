package jgh.artistex.engine.visitors;

import android.graphics.Paint;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.Layers.Bitmaps.BaseBitmapLayer;
import jgh.artistex.engine.Layers.Bitmaps.TextLayer;
import jgh.artistex.engine.Layers.Pens.BasePen;


public class JoinTypeVisitor implements ILayerVisitor {

    private Paint.Join mJoin;

    public JoinTypeVisitor(Paint.Join join){
        mJoin = join;
    }
    @Override
    public void visit(ILayer layer) {

    }

    @Override
    public void visitPen(BasePen basePen) {
        basePen.setJoinType(mJoin);
    }

    @Override
    public void visitBitmap(BaseBitmapLayer layer) {

    }


    @Override
    public void visitText(TextLayer layer) {
        layer.setJoinType(mJoin);
    }
}
