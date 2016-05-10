package jgh.artistex.engine.visitors;

import jgh.artistex.engine.ILayer;
import jgh.artistex.engine.Layers.Bitmaps.BaseBitmapLayer;
import jgh.artistex.engine.Layers.Bitmaps.TextLayer;
import jgh.artistex.engine.Layers.Pens.BasePen;

/**
 * Visitor class for selecting mode of a layer. Used for classes which extend
 * either <code>BasePen</code> or <code>BasePolygon</code>, mainly.
 */
public class ModeSelectVisitor implements ILayerVisitor {


    /**
     *
     */
    public enum Mode {
        DRAW, VERTEX, BEZIER, ROTATE, TRANSLATE, SCALE
    }


    /**
     *
     */
    private Mode mMode;

    /**
     *
     */
    public ModeSelectVisitor() {

    }


    /**
     * Sets the Mode for <code>ILayer</code> actions.
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        mMode = mode;
    }

    @Override
    public void visit(ILayer layer) {
        layer.accept(this);
    }

    @Override
    public void visitPen(BasePen basePen) {
        if (basePen == null)
            return;

        switch (mMode) {
            case DRAW:
                basePen.setState(BasePen.State.DRAWING);
                break;
            case BEZIER:
                basePen.setState(BasePen.State.BEZIER);
                break;
            case VERTEX:
                basePen.setState(BasePen.State.VERTEX);
                break;
            case ROTATE:
                basePen.setState(BasePen.State.ROTATING);
                break;
            case SCALE:
                basePen.setState(BasePen.State.SCALING);
                break;
            case TRANSLATE:
                basePen.setState(BasePen.State.MOVING);
                break;
            default:
                break;
        }
    }

    @Override
    public void visitBitmap(BaseBitmapLayer layer) {

    }


    @Override
    public void visitText(TextLayer layer) {

    }
}
