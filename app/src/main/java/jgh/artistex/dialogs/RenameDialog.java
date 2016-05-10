package jgh.artistex.dialogs;


import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import jgh.artistex.R;
import jgh.artistex.engine.utils.Toaster;

/**
 *
 */
public class RenameDialog extends BasicDialog implements View.OnClickListener {
    Button mCompleteButton;
    EditText mRenameEditText;

    private String mPath;
    private String mExt ="";

    public RenameDialog(Activity activity, String path, String extension) {
        super(activity, R.layout.dialog_rename);

        mCompleteButton = (Button) getLayout().findViewById(R.id.completebutton);
        mRenameEditText = (EditText) getLayout().findViewById(R.id.rename_edittext);

        mCompleteButton.setOnClickListener(this);
        mPath = path;
        mExt = extension;
        getDialog().show();

    }

    @Override
    public void onClick(View v) {

        String str = mRenameEditText.getText().toString().trim();
        if (str == null || str.isEmpty()) {
            Toaster.makeShortToast(getActivity(), "Unable to rename.");

        }
        else {
            try {
                String[] splitter = mPath.split("/");
                if (splitter == null || splitter.length <= 1) {
                    Toaster.makeShortToast(getActivity(), "Unable to rename.");
                }
                else {
                    String newPath = "";
                    /* limit for-loop to the second to last item */
                    for (int count = 0; count < splitter.length - 1; count++) {
                        newPath += "/" + splitter[count];
                    }
                    newPath += "/" + str;
                    if(!newPath.endsWith(mExt)){
                        newPath+=mExt;
                    }

                    File f = new File(mPath);
                    if (!f.exists()) {
                        Toaster.makeShortToast(getActivity(), "Unable to rename.");
                    }
                    else {
                        boolean b = f.renameTo(new File(newPath));
                        if (!b){
                            Toaster.makeShortToast(getActivity(), "Unable to rename.");
                        }
                    }
                    getDialog().dismiss();
                }
            } catch (Exception e) {
                Toaster.makeShortToast(getActivity(), "Unable to rename.");
            }
        }

    }

}
