package jgh.artistex.engine.visitors;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.Layers.Bitmaps.BaseBitmapLayer;
import jgh.artistex.engine.Layers.Bitmaps.TextLayer;
import jgh.artistex.engine.Layers.Pens.BasePen;

/**
 * Visitor for ILayers implementations.
 */
public interface ILayerVisitor {

    void visit(ILayer layer);

    /**
     * Visit layers extending the <code>BasePen</code> layer.
     *
     * @param basePen <code>BasePen</code> layer.
     */
    void visitPen(BasePen basePen);


    /**
     * Visits <code>BitmapLayer</code> layers.
     *
     * @param layer
     */
    void visitBitmap(BaseBitmapLayer layer);


    /**
     * Visits <code>TextLayer</code> layers.
     *
     * @param layer
     */
    void visitText(TextLayer layer);
}
