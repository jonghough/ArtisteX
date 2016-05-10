package jgh.artistex.layers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jgh.artistex.engine.Layers.Bitmaps.BitmapLayer;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import jgh.artistex.BuildConfig;
import jgh.artistex.R;
import jgh.artistex.engine.DrawingEngine;
import jgh.artistex.engine.IconManager;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class BitmapLayerTest{


    @Test
    public void bitmapLayerTest1(){
//        DrawingEngine.Instance().killEngine();
//
//        Bitmap r = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
//                R.drawable.rotate20);
//        Bitmap s = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
//                R.drawable.scale20);
//        Bitmap t = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
//                R.drawable.translate20);
//
//        IconManager.instance().initialize(r, s, t);
//
//        BitmapLayer bitmapLayer = new BitmapLayer(null, 0,0);
//        Field field = bitmapLayer.getClass().getDeclaredField("mMatrix");
//        field.setAccessible(true);
//        Matrix m = (Matrix)field.get(bitmapLayer);

    }

}