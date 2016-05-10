package jgh.artistex.engine;

/**
 * Class for handling an Undoable action.
 */
public final class IUndo {

    /**
     * The <code>UndoHandler</code> performs the
     * actual undo and redo operations, defined per implementation.
     */
    private IUndoHandler mUndoHandler;


    /**
     * Creates an instance of IUndo. A non-null argument must be passed, or an
     * <code>IllegalArgumentException</code> will be thrown.
     *
     * @param undoHandler the undo handler, hadnles the specifics of the undo and redo actions.
     */
    public IUndo(IUndoHandler undoHandler) {
        if (undoHandler == null) throw new IllegalArgumentException("Cannot pass null argument.");
        mUndoHandler = undoHandler;
    }


    /**
     * Undo action -- should only be called in <code>DrawingEngine</code> because
     * it should be applied after this <code>IUndo</code> instance is  popped
     * off the undo stack <b>only</b>.
     */
    /* default */ void undo() {
        mUndoHandler.handleUndo();
    }


    /**
     * Redo action -- should only be called in <code>DrawingEngine</code>
     */
    /* default */ void redo() {
        mUndoHandler.handleRedo();
    }
}
