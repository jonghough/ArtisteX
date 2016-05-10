package jgh.artistex.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jgh.artistex.R;

/**
 * Text picker dialog for <code>TextLayer</code> objects.
 */
public class TextPickerDialog extends BasicDialog implements View.OnClickListener {

    private String mText = "";
    private EditText mEditText;
    private Button mButton;

    private TextPickerCallbackHandler mCallback;

    public TextPickerDialog(Activity activity, String defaultText, TextPickerCallbackHandler callback) {
        super(activity, R.layout.dialog_textpicker);
        mText = defaultText;
        mCallback = callback;
        mEditText = (EditText) getLayout().findViewById(R.id.textpicker);
        mButton = (Button) getLayout().findViewById(R.id.selectbutton);
        mButton.setOnClickListener(this);

        getDialog().show();
    }

    @Override
    public void onClick(View v) {

        mText = mEditText.getText().toString();
        if (mText == null || mText.isEmpty()) {
            return;
        } else {
            mCallback.onTextChosen(mText);

            getDialog().dismiss();
        }
    }

    public interface TextPickerCallbackHandler {

        void onTextChosen(String text);
    }
}

