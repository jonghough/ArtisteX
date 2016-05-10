package jgh.artistex.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import jgh.artistex.R;

/**
 *
 */
public class SingleSliderDialog extends BasicDialog implements View.OnClickListener {

    private String mSlidertText, mButtonText;
    private SeekBar mSeekBar;
    private Button mButton;
    private OnClickHandler mOnClickHandler;

    public SingleSliderDialog(Activity activity, int defaultValue, String sliderText, String buttonText, OnClickHandler onClickHandler) {
        super(activity, R.layout.dialog_singleslider);

        mSlidertText = sliderText;
        mButtonText = buttonText;

        mButton = (Button) getLayout().findViewById(R.id.selectbutton);
        mButton.setText(mButtonText);
        mButton.setOnClickListener(this);

        mSeekBar = (SeekBar) getLayout().findViewById(R.id.seekbar);
        mSeekBar.setProgress(defaultValue);

        ((TextView) getLayout().findViewById(R.id.seekbartext)).setText(mSlidertText);

        mOnClickHandler = onClickHandler;

        getDialog().show();
    }

    @Override
    public void onClick(View v) {
        if (v == mButton) {
            mOnClickHandler.onClickWithInt(mSeekBar.getProgress());
            getDialog().dismiss();
        }
    }
}
