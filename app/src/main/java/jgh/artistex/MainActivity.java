package jgh.artistex;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;

import jgh.artistex.dialogs.LayerOptionsCallbackHandler;
import jgh.artistex.dialogs.ShapeDialog;
import jgh.artistex.dialogs.ShapeSetCallbackHandler;
import jgh.artistex.engine.Layers.Bitmaps.BitmapLayer;
import jgh.artistex.engine.Layers.Bitmaps.TextLayer;
import jgh.artistex.engine.utils.Toaster;
import jgh.artistex.engine.visitors.ModeSelectVisitor;
import jgh.artistex.engine.Layers.Pens.Pen;
import jgh.artistex.engine.Layers.Pens.Pencil;
import jgh.artistex.engine.Layers.Pens.Polygon;
import jgh.artistex.engine.Layers.Pens.PolygonFactory;
import jgh.artistex.dialogs.ColorPickerDialog;
import jgh.artistex.dialogs.ColorSetCallbackHandler;
import jgh.artistex.dialogs.LayerListDialog;
import jgh.artistex.dialogs.OnClickHandler;
import jgh.artistex.dialogs.SingleSliderDialog;
import jgh.artistex.dialogs.TextPickerDialog;
import jgh.artistex.dialogs.UserFileViewDialog;
import jgh.artistex.engine.IconManager;
import jgh.artistex.engine.utils.FileUtils;

/**
 * The activity in which the drawing happens.
 */
public class MainActivity extends Activity {

    /**
     * For debug logs
     */
    private static final String TAG = "MainActivity";

    /**
     * The view for drawing on canvas.
     */
    private DrawingView mView;


    private Intent mData;
    /**
     * Gallery
     */
    protected static final int GALLERY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = (DrawingView) findViewById(R.id.drawing_view);

        Bitmap r = BitmapFactory.decodeResource(getResources(),
                R.drawable.rotate20);
        Bitmap s = BitmapFactory.decodeResource(getResources(),
                R.drawable.scale20);
        Bitmap t = BitmapFactory.decodeResource(getResources(),
                R.drawable.translate20);

        IconManager.instance().initialize(r, s, t);

        FileUtils.makeImagesDirectory();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.showbounding);
        if (item != null) {
            if (mView.getShowTransformBox())
                item.setTitle("Hide bounding box");
            else
                item.setTitle("Show bounding box");
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.showbounding);
        if (item != null) {
            if (mView.getShowTransformBox())
                item.setTitle("Hide bounding box");
            else
                item.setTitle("Show bounding box");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.drawing) {
            ModeSelectVisitor v = new ModeSelectVisitor();
            v.setMode(ModeSelectVisitor.Mode.DRAW);
            mView.visitLayer(v);
        } else if (id == R.id.control_points) {
            ModeSelectVisitor v = new ModeSelectVisitor();
            v.setMode(ModeSelectVisitor.Mode.BEZIER);
            mView.visitLayer(v);
        } else if (id == R.id.vertex) {
            ModeSelectVisitor v = new ModeSelectVisitor();
            v.setMode(ModeSelectVisitor.Mode.VERTEX);
            mView.visitLayer(v);
        } else if (id == R.id.strokecolor) {
            new ColorPickerDialog(this, mView.getStrokeColor(), new ColorSetCallbackHandler() {

                @Override
                public void setColor(int color) {
                    mView.setStrokeColor(color);
                    mView.invalidate();
                }
            });
        } else if (id == R.id.strokethickness) {
            new SingleSliderDialog(this, 1, "Thickness", "Select", new OnClickHandler() {
                @Override
                public void onClickWithInt(int value) {
                    mView.setStrokeThickness(value);
                    mView.invalidate();
                }

                @Override
                public void onClickWithFloat(float value) {
                    //not used.
                }
            });
        } else if (id == R.id.fillcolor) {
            new ColorPickerDialog(this, mView.getFillColor(), new ColorSetCallbackHandler() {

                @Override
                public void setColor(int color) {
                    mView.setFillColor(color);
                    mView.invalidate();
                }
            });
        } else if (id == R.id.backgroundcolor) {
            new ColorPickerDialog(this, mView.getBackgroundColor(), new ColorSetCallbackHandler() {

                @Override
                public void setColor(int color) {
                    mView.setBackgroundColor(color);
                    mView.invalidate();
                }
            });
        } else if (id == R.id.copy) {
            mView.copy();
        } else if (id == R.id.undo) {
            mView.undo();
        } else if (id == R.id.image) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, GALLERY);
        } else if (id == R.id.pen) {
            Pen p = new Pen();
            mView.setLayer(p);
        } else if (id == R.id.pencil) {
            Pencil p = new Pencil();
            mView.setLayer(p);
        } else if (id == R.id.polygon) {
            new ShapeDialog(this, new ShapeSetCallbackHandler() {
                @Override
                public void onShapeSet(int vertexSize, int type) {
                    if (type == 0) {
                        ArrayList<PointF> p = PolygonFactory.createShapeStar(vertexSize, 100, 100, 100);
                        mView.setLayer(new Polygon(p));
                    } else if (type == 1) {
                        ArrayList<PointF> p = PolygonFactory.createPolygon(vertexSize, 100, 100, 100);
                        mView.setLayer(new Polygon(p));
                    }
                }


            });

        } else if (id == R.id.text) {
            new TextPickerDialog(this, "Default", new TextPickerDialog.TextPickerCallbackHandler() {
                @Override
                public void onTextChosen(String text) {
                    TextLayer ep = new TextLayer(text, Color.RED, 10, Typeface.MONOSPACE);
                    mView.setLayer(ep);
                }
            });

        } else if (id == R.id.eraser) {
            //TODO
        } else if (id == R.id.viewbitmaps) {
            new UserFileViewDialog(this, UserFileViewDialog.FileType.IMAGE);
        } else if (id == R.id.viewsvgs) {
            new UserFileViewDialog(this, UserFileViewDialog.FileType.SVG);
        } else if (id == R.id.savebitmap) {
            mView.saveBitmapData();
        } else if (id == R.id.savesvg) {
            mView.saveSvgData();
        } else if (id == R.id.listlayers) {
            new LayerListDialog(this, R.layout.dialog_layerlist, mView.getLayerNameList(), new DefaultLayerOptionsCallbackHandler());

        } else if (id == R.id.showbounding) {
            mView.setShowTransformBox(!mView.getShowTransformBox());
        } else if (id == R.id.clearall) {
            mView.clearCanvas();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Launches photo picker activity.
     */
    public void getPicFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, GALLERY);

    }


    private void loadImage() {
        Uri selectedImageUri = mData.getData();
        int imageId = getCursorIndex(selectedImageUri);
        Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(
                getApplicationContext().getContentResolver(), imageId,
                MediaStore.Images.Thumbnails.MINI_KIND, null);

        bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(),
                false);


        BitmapLayer bml = new BitmapLayer(bmp, 50f, 50f);
        mView.setLayer(bml);
    }

    /**
     * Callback for photo picker activity. After picture is chosen we
     * need to get the bitmap and scale it to a reasonable size. Bitmap will then be added to the
     * Drawing view.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            Log.e(TAG, "Error, intent is null");
        } else {
            mData = data;
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        101);
            } else {
                loadImage();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage();


                } else {
                    Toaster.makeLongToast(this, "NO permissions");
                }
                break;
            }
            default:
                break;
        }
    }


    public DrawingView getView() {
        return mView;
    }


    /**
     * @param uri
     * @return
     */
    public int getCursorIndex(Uri uri) {

        String[] projection = {
                MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA
        };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            return id;
        } else
            return -1;
    }

    private class DefaultLayerOptionsCallbackHandler implements LayerOptionsCallbackHandler {

        @Override
        public void select(int index) {

        }

        @Override
        public void delete(int index) {
            mView.delete(index);
        }

        @Override
        public void rename(int index) {

        }
    }

}
