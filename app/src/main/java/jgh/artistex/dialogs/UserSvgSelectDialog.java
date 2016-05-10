package jgh.artistex.dialogs;


import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import jgh.artistex.DrawingView;
import jgh.artistex.R;
import jgh.artistex.engine.utils.DataUtils;

public class UserSvgSelectDialog extends BasicDialog implements View.OnClickListener {
    Button mSendButton, mDeleteButton, mRenameButton;

    private String mPath;

    public UserSvgSelectDialog(Activity activity, String path) {
        super(activity, R.layout.dialog_svgselect);

        mSendButton = (Button) getLayout().findViewById(R.id.sendbutton);
        mRenameButton = (Button)getLayout().findViewById(R.id.renamebutton);
        mDeleteButton = (Button) getLayout().findViewById(R.id.deletebutton);
        mSendButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mRenameButton.setOnClickListener(this);
        mPath = path;
        getDialog().show();
    }

    @Override
    public void onClick(View v) {
        if (v == mSendButton) {
            ArrayList<Uri> urilist = new ArrayList<Uri>();
            urilist.add(Uri.fromFile(new File(mPath)));
            DataUtils.sendMail(getActivity(), "SVG file", "SVG created by ArtisteX!", "", urilist);

        }
        else if(v == mRenameButton){
            new RenameDialog(getActivity(), mPath, ".svg");
        }
        else if(v == mDeleteButton){
            File f = new File(mPath);
            if(f.exists()){
                f.delete();
            }
        }

        getDialog().dismiss();

    }



}