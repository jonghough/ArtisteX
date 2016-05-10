package jgh.artistex.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import jgh.artistex.R;
import jgh.artistex.views.PaletteView;

/**
 * Dialog class for choosing color. Dialog contains sliders for ARGB selection,
 * and a <code>View</code> to see the current color.
 * The <code>ColorSetCallbackHandler</code> object handles what to do
 * when the color is chosen.
 */
public final class ColorPickerDialog extends BasicDialog implements View.OnClickListener {

    /*
     * Seekbars for the ARGB channels.
     */
    private SeekBar mAlphaSeek, mRedSeek, mGreenSeek, mBlueSeek;

    /**
     * Select button for finalizing the current selection.
     */
    private Button mSelectButton;

    /**
     * The given color. i.e. the default color
     */
    private int mColor;

    /**
     * Currently selected ARGB values.
     */
    private int mAlpha, mRed, mGreen, mBlue;

    private ColorSetCallbackHandler mCallbackHandler;

    private PaletteView mPaletteView;

    /**
     * @param activity - mainactivity
     * @param color    - current color value
     */
    public ColorPickerDialog(Activity activity, int color, ColorSetCallbackHandler callbackHandler) {
        super(activity, R.layout.dialog_colorpicker);
        mCallbackHandler = callbackHandler;
        mColor = color;

        /*
         * Extract each ARGB channel form the color
         */
        mAlpha = (color >> 24) & 0xFF;
        mRed = (color & 0xFF0000) >> 16;
        mGreen = (color & 0x0000FF00) >> 8;
        mBlue = (color & 0x000000FF);

        // get the seekbar references
        mAlphaSeek = (SeekBar) getLayout().findViewById(R.id.alphaseekbar);
        mRedSeek = (SeekBar) getLayout().findViewById(R.id.redseekbar);
        mGreenSeek = (SeekBar) getLayout().findViewById(R.id.greenseekbar);
        mBlueSeek = (SeekBar) getLayout().findViewById(R.id.blueseekbar);

        //get the paletteview referecne and set to default color.
        mPaletteView = (PaletteView) getLayout().findViewById(R.id.palette);
        mPaletteView.setColor(mColor);

        mSelectButton = (Button) getLayout().findViewById(R.id.selectbutton);
        mSelectButton.setOnClickListener(this);

        // set the seekbar default values.
        mAlphaSeek.setProgress(mAlpha);
        mRedSeek.setProgress(mRed);
        mGreenSeek.setProgress(mGreen);
        mBlueSeek.setProgress(mBlue);

        mAlphaSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                mAlpha = mAlpha << 24;
                mColor = (mColor & 0x00ffffff) | mAlpha;

                mPaletteView.setColor(mColor);
                mPaletteView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

        });

        mRedSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mRed = progress;
                mRed = mRed << 16;
                mColor = (mColor & 0xff00ffff) | mRed;

                mPaletteView.setColor(mColor);
                mPaletteView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

        });

        mGreenSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mGreen = progress;
                mGreen = mGreen << 8;
                mColor = (mColor & 0xffff00ff) | mGreen;

                mPaletteView.setColor(mColor);
                mPaletteView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

        });

        mBlueSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mBlue = progress;
                mColor = (mColor & 0xffffff00) | mBlue;

                mPaletteView.setColor(mColor);
                mPaletteView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

        });

        getDialog().show();
    }

    @Override
    public void onClick(View v) {
        if (v == mSelectButton) {

            mCallbackHandler.setColor(mColor);
            getDialog().dismiss();
        }


    }

}