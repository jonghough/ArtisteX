package jgh.artistex.dialogs;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

import jgh.artistex.R;
import jgh.artistex.adapters.FileListAdapter;
import jgh.artistex.adapters.ImageAdapter;
import jgh.artistex.engine.utils.FileUtils;

/**
 * Dialog for viewing some user generated files. Either SVG files or
 * Bitmap files.
 */
public class UserFileViewDialog extends BasicDialog implements AdapterView.OnItemClickListener {

        private GridView mGridView;
        private BaseAdapter mAdapter;
        private String mChosenPath;
        private ArrayList<String> mFilenames;

        public enum FileType {
            IMAGE, SVG
        };

        private FileType mFileType = FileType.IMAGE;

        /**
         *
         * @param activity
         * @param filetype
         */
        public UserFileViewDialog(Activity activity, FileType filetype) {
            super(activity, R.layout.dialog_userfiles);
            mFileType = filetype;
            if (mFileType == FileType.IMAGE) {
                mFilenames = FileUtils.getUserImages();
                mAdapter = new ImageAdapter(getActivity(), mFilenames);
            }
            else {
                mFilenames = FileUtils.getUserSVGFiles();
                mAdapter = new FileListAdapter(getActivity(), mFilenames);
            }
            mGridView = (GridView) getLayout().findViewById(R.id.filesgridview);

            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(this);

            this.getDialog().show();
        }

        /**
         *
         */
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            if (mFilenames == null || position >= mFilenames.size())
                return;
            mChosenPath = mFilenames.get(position);

            if (mChosenPath == null || mChosenPath == "")
                return;
            if (mFileType == FileType.IMAGE) {

                new UserFileSelectDialog(getActivity(), mChosenPath);
            }
            else if (mFileType == FileType.SVG) {
                new UserSvgSelectDialog(getActivity(), mChosenPath);
            }
            getDialog().dismiss();

        }

    }