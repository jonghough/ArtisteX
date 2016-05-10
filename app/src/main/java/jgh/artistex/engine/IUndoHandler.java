package jgh.artistex.engine;

/**
 * Interface for handling <code>IUndo</code> undo and redo functions. Implementations of this
 * interface should define specific behaviour for <code>handleUndo</code> and
 * <code>handleRedo</code> methods, and the instance should be passed to a new instance of
 * <code>IUndo</code>. The drawing engine keeps a sack of <code>IUndo</code> objects and calls the
 * undo methods when the user requests an undo.
 */
public interface IUndoHandler {

    /**
     * Handles the undo action.
     */
    void handleUndo();

    /**
     * Handles the redo action
     */
    void handleRedo();
}
