package jgh.artistex.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import jgh.artistex.R;

public class FileListAdapter extends BaseAdapter {

    private ArrayList<String> mFiles;
    private Context mContext;
    private LayoutInflater mInflater;

    //
    // Constructor
    //
    public FileListAdapter(Context context, ArrayList<String> mFilenames) {
        mContext = context;
        mFiles = mFilenames;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mFiles.size();
    }

    public Object getItem(int index) {
        return mFiles.get(index);
    }

    public long getItemId(int index) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        File file = new File(mFiles.get(position));

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.item_listview, null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.webview_item);
            holder.imageView.setFocusable(false);
            holder.imageView.setClickable(false);
            holder.textView = (TextView) convertView.findViewById(R.id.textview_item);
            // change text color for directories
            if (file != null && file.isDirectory()) {
                holder.textView.setTextColor(0xff009999);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
            // change text color for directories
            if (file != null && file.isDirectory()) {
                holder.textView.setTextColor(0xff009999);
            }
        }

        /* only want the last part of the file path - the file name */
        String[] splitter = mFiles.get(position).split("/");



        if (splitter != null && splitter.length > 0) {
            holder.textView.setText(splitter[splitter.length - 1]);
        }
        convertView.setTag(holder);
        return convertView;
    }

    /**
     * Private view holder class
     *
     */
    private class ViewHolder {
        ImageView imageView;

        TextView textView;
    }

}