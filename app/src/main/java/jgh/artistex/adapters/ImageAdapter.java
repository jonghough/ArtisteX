package jgh.artistex.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import jgh.artistex.R;

public class ImageAdapter extends BaseAdapter {

    private ArrayList<String> mFiles;
    private Context mContext;
    private LayoutInflater mInflater;

    private ThreadPoolExecutor mExecutor;

    //
    // Constructor
    //
    public ImageAdapter(Context context, ArrayList<String> files) {
        mContext = context;
        mFiles = files;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }

    public int getCount() {
        return mFiles.size();
    }

    public Object getItem(int index) {
        return mFiles.get(index);
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;// = new ViewHolder();
        if (convertView == null || !isCorrectPos(convertView, position)) {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.item_userfiles, null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview_item);
            holder.imageView.setTag(position);
            loadImage(position, holder.imageView);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.imageView.setTag(position);
            loadImage(position, holder.imageView);
        }

        convertView.setTag(holder);
        return convertView;
    }

    /**
     * Tests if the convertView position is correct
     * @param convertView
     * @param position
     * @return
     */
    private boolean isCorrectPos(View convertView, int position) {
        ViewHolder vh = (ViewHolder) convertView.getTag();
        if (position != (Integer) vh.imageView.getTag()) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param position
     * @param view
     */
    private void loadImage(int position, ImageView view) {
        final Handler handler = new ImageHandler(position, view);
        final String path = mFiles.get(position);

        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                Bitmap bmp = BitmapFactory.decodeFile(path);
                bmp = ThumbnailUtils.extractThumbnail(bmp, 20, 25);

                Bundle data = new Bundle();
                data.putParcelable("image", bmp);
                Message msg = new Message();
                msg.setData(data);
                handler.sendMessage(msg);

            }

        });
    }

    private class ImageHandler extends Handler {
        private int mPosition;
        private ImageView mView;

        public ImageHandler(int position, ImageView view) {
            mPosition = position;
            mView = view;

        }

        @Override
        public void handleMessage(Message msg) {
            int pos = (Integer) mView.getTag();
            if (pos != mPosition) {
                return;
            }

            Bitmap bmp = msg.getData().getParcelable("image");
            mView.setImageBitmap(bmp);
        }
    }

    /**
     * Private view holder class
     *
     * @author Jonathan Hough
     */
    private class ViewHolder {

        ImageView imageView;
    }

}