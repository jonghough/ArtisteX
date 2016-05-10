package jgh.artistex.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import jgh.artistex.R;
import jgh.artistex.engine.utils.Toaster;


public class ShapeDialog extends BasicDialog implements View.OnClickListener{

    private Button mButton;
    private RadioGroup mRadioGroup;
    private EditText mEditText;
    private ShapeSetCallbackHandler mOnClickHandler;

    /**
     * Instantiates a dialog instance.
     *
     * @param activity   caller activity
     */
    public ShapeDialog(Activity activity, ShapeSetCallbackHandler onClickHandler) {
        super(activity, R.layout.dialog_shape);

        mRadioGroup = (RadioGroup)getLayout().findViewById(R.id.shape_group);
        mButton = (Button)getLayout().findViewById(R.id.selectbutton);
        mEditText = (EditText) getLayout().findViewById(R.id.numberpicker);
        mButton.setOnClickListener(this);

        mOnClickHandler = onClickHandler;
        getDialog().show();
    }


    @Override
    public void onClick(View v) {
        int id = mRadioGroup.getCheckedRadioButtonId();
        if(mEditText.getText().toString() == null || mEditText.getText().toString().isEmpty()){
            Toaster.makeShortToast(getActivity(), "Please input number of points.");
        }
        else {
            int type;
            if(mRadioGroup.getCheckedRadioButtonId() == R.id.star_radio)
                type = 0;
            else
            type = 1;
            mOnClickHandler.onShapeSet(Integer.parseInt(mEditText.getText().toString()), type);
            getDialog().dismiss();
        }
    }

}
