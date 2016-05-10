package jgh.artistex.layers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.SystemClock;
import android.view.MotionEvent;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import jgh.artistex.BuildConfig;
import jgh.artistex.R;
import jgh.artistex.engine.DrawingEngine;
import jgh.artistex.engine.IconManager;
import jgh.artistex.engine.Layers.Pens.BasePen;
import jgh.artistex.engine.Layers.Pens.PolygonFactory;
import jgh.artistex.engine.Layers.Pens.Polygon;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class PolygonTest {

    @Test
    public void polygonTest1() {
        DrawingEngine.Instance().killEngine();

        Bitmap r = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.rotate20);
        Bitmap s = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.scale20);
        Bitmap t = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.translate20);

        IconManager.instance().initialize(r,s,t);


        BasePen p = new Polygon(PolygonFactory.createPolygon(10, 100,100,100));

        DrawingEngine.Instance().setLayer(p);
        ArrayList<PointF> list = null;
        try {
            Method method = BasePen.class.getDeclaredMethod("getVertexList", null);
            method.setAccessible(true);
            list = (ArrayList<PointF>) method.invoke(p, new Object[]{});
            Assert.assertTrue(list.size() == 10);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException exx) {
            exx.printStackTrace();
        } catch (IllegalAccessException exxx) {
            exxx.printStackTrace();
        }finally{
            DrawingEngine.killEngine();
        }
        Assert.assertNotNull(list);
    }

}