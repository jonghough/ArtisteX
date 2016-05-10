package jgh.artistex.engine.Layers.Bitmaps;

import jgh.artistex.engine.IUndoHandler;


/**
 *
 */
public class ColorUndo implements IUndoHandler {

    public TextLayer layer;
    public int preColor;
    public int postColor;

    @Override
    public void handleUndo() {
        if (layer != null)
            layer.setColorNoUndo(preColor);
    }

    @Override
    public void handleRedo() {

        if (layer != null)
            layer.setColorNoUndo(postColor);
    }
}
