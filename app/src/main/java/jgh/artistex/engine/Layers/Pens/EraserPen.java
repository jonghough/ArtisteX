package jgh.artistex.engine.Layers.Pens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Extension of <code>Pen</code> class, for eraser effect.
 */
public class EraserPen extends Pen {


    /**
     *
     */
    public EraserPen() {
        super();
        mStrokePaint = new Paint(Color.GREEN);
        mStrokePaint.setStrokeWidth(30);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        //  mStrokePaint.setAlpha(0);
        mStrokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
}
