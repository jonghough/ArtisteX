package jgh.artistex.engine;

import android.graphics.Bitmap;

/**
 * Singleton(ish) for holding the RST (rotate, scale, translate) icons. Icons are
 * used for performing RST transforms of <code>ILayer</code>s.
 */
public final class IconManager {


    /**
     * icon manager instance
     */
    private static IconManager sIconManager;


    /**
     * The icons for rotate, scale and translate.
     */
    private LayerIconHolder mIconRotate, mIconScale, mIconTranslate;


    /**
     * private constructor
     */
    private IconManager() {
    }


    /**
     * Private constructor for the IconManager.
     *
     * @param rotBmp   the rotate icon bitmap
     * @param scaleBmp the scale icon bitmap
     * @param transBmp the translate icon bitmap
     */
    private void init(Bitmap rotBmp, Bitmap scaleBmp, Bitmap transBmp) {
        mIconRotate = new LayerIconHolder(rotBmp);
        mIconScale = new LayerIconHolder(scaleBmp);
        mIconTranslate = new LayerIconHolder(transBmp);
    }


    /**
     * Returns the instance of the IconManager. At the start of the app's life cycle
     * the IconManager's bitmaps icons should be setup first, and this should only be called
     * subsequently.
     *
     * @return instance of the <code>IconManager</code>
     */
    public static IconManager instance() {
        if (sIconManager == null)
            sIconManager = new IconManager();
        return sIconManager;
    }


    /**
     * Initializes the singleton instance with the bitmaps necessary for rotating, scaling and
     * translating layers.
     * @param rotBmp   the rotate icon bitmap
     * @param scaleBmp the scale icon bitmap
     * @param transBmp the translate icon bitmap
     */
    public void initialize(Bitmap rotBmp, Bitmap scaleBmp, Bitmap transBmp) {
        init(rotBmp, scaleBmp, transBmp);

    }


    /**
     * Gets the <i>Rotate</i> icon LayerIconHolder.
     *
     * @return rotate icon
     */
    public LayerIconHolder getRotationIcon() {
        return mIconRotate;
    }


    /**
     * Gets the <i>Scale</i> icon LayerIconHolder.
     *
     * @return scale icon
     */
    public LayerIconHolder getScaleIcon() {
        return mIconScale;
    }


    /**
     * Gets the <i>Translate</i> icon LayerIconHolder.
     *
     * @return translate icon
     */
    public LayerIconHolder getTranslateIcon() {
        return mIconTranslate;
    }


    /**
     * Sets the icon coordinates, defined by the <code>SpecialRectF</code> bounds.
     * The translate icon will be set at the top left position, the rotate icon will be set at
     * the bottom left position and the scale icon will be set at the bototm right position.
     *
     * @param mBounds The boundary rect around the current <code>ILayer</code> object.
     */
    public void setIconCoords(SpecialRectF mBounds) {

        mIconRotate.setXCoord(mBounds.left);
        mIconRotate.setYCoord(mBounds.bottom);

        mIconScale.setXCoord(mBounds.right);
        mIconScale.setYCoord(mBounds.bottom);

        mIconTranslate.setXCoord(mBounds.left);
        mIconTranslate.setYCoord(mBounds.top);
    }

    public void setShowingIcons(boolean showing){
        mIconRotate.setShowing(showing);
        mIconScale.setShowing(showing);
        mIconTranslate.setShowing(showing);
    }

}
