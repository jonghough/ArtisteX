package jgh.artistex.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class PaletteView extends View {

    private int mColor;

    public PaletteView(Context context) {
        super(context);
        init();
    }

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaletteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setBackgroundColor(mColor);
    }

    public void setColor(int color) {
        mColor = color;
    }

    protected void onDraw(Canvas canvas) {
        this.setBackgroundColor(mColor);
    }

}