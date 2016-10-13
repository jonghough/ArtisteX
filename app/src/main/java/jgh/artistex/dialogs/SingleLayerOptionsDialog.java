package jgh.artistex.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import jgh.artistex.R;

public class SingleLayerOptionsDialog extends BasicDialog {

    private int mIndex;
    private Button mDeleteButton;
    private LayerOptionsCallbackHandler mCallbackHandler;
    /**
     * Instantiates a dialog instance.
     *
     * @param activity   caller activity
     */
    public SingleLayerOptionsDialog(Activity activity, int index, LayerOptionsCallbackHandler callbackHandler) {
        super(activity, R.layout.dialog_singlelayeroptions);

        mIndex = index;
        mCallbackHandler = callbackHandler;
        mDeleteButton = (Button)getLayout().findViewById(R.id.deletebutton);

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackHandler.delete(mIndex);
                getDialog().dismiss();
            }
        });
        getDialog().show();
    }

}
