package jgh.artistex.dialogs;


import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import jgh.artistex.engine.Layers.Bitmaps.BitmapLayer;
import jgh.artistex.MainActivity;
import jgh.artistex.R;
import jgh.artistex.engine.utils.DataUtils;
import jgh.artistex.engine.utils.FileUtils;

public class UserFileSelectDialog extends BasicDialog implements View.OnClickListener {

         Button mThumbButton, mSendButton;

        private String mPath;

        public UserFileSelectDialog(Activity activity, String path) {
            super(activity, R.layout.dialog_photooptions);

            mThumbButton = (Button) getLayout().findViewById(R.id.thumbbutton);
            mSendButton= (Button) getLayout().findViewById(R.id.sendbutton);
            mThumbButton.setOnClickListener(this);
            mSendButton.setOnClickListener(this);
            mPath = path;
            getDialog().show();
        }

        @Override
        public void onClick(View v) {
            if (v == mThumbButton) {
                Bitmap bmp = FileUtils.getThumbnailBitmapFromUserFile(mPath);
                BitmapLayer bml = new BitmapLayer(bmp, 50f, 50f);
                ((MainActivity) getActivity()).getView().setLayer(bml);
            }

            else if (v == mSendButton){
                ArrayList<Uri> urilist = new ArrayList<Uri>();
                urilist.add(Uri.fromFile(new File(mPath)));
                DataUtils.sendMail(getActivity(), "Picture file", "Picture created by ArtisteX!", "", urilist);
            }
            getDialog().dismiss();
        }

    }