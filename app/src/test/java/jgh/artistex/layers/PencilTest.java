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
import jgh.artistex.engine.Layers.Pens.Pen;
import jgh.artistex.engine.Layers.Pens.Pencil;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class PencilTest {

    @Test
    public void pencilTest1(){
        //no touches, vertex list should be initialized but empty.

        DrawingEngine.Instance().killEngine();

        Bitmap r = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.rotate20);
        Bitmap s = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.scale20);
        Bitmap t = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.translate20);

        IconManager.instance().initialize(r, s, t);
        BasePen p = new Pencil();

        DrawingEngine.Instance().setLayer(p);
        ArrayList<PointF> list = null;
        try {
            Method method = BasePen.class.getDeclaredMethod("getVertexList", null);
            method.setAccessible(true);
            list = (ArrayList<PointF>) method.invoke(p, new Object[]{});
            Assert.assertTrue(list.size() == 0);
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

    @Test
    public void pencilTest2(){
        DrawingEngine.Instance().killEngine();

        Bitmap r = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.rotate20);
        Bitmap s = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.scale20);
        Bitmap t = BitmapFactory.decodeResource(RuntimeEnvironment.application.getApplicationContext().getResources(),
                R.drawable.translate20);

        IconManager.instance().initialize(r, s, t);
        MotionEvent e1 = MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                10, 11, 0);
        MotionEvent e2 = MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP,
                10, 11, 0);
        MotionEvent e3 = MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                100, 110, 0);

        MotionEvent e4 = MotionEvent.obtain( SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP,
                100,110, 0);
        MotionEvent e5 = MotionEvent.obtain( SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                109,90, 0);


        BasePen p = new Pencil();




        DrawingEngine.Instance().setLayer(p);
        DrawingEngine.Instance().onMotionEvent(e1); //first touch
        DrawingEngine.Instance().onMotionEvent(e2);
        DrawingEngine.Instance().onMotionEvent(e3); //second touch -- vertex is added
        DrawingEngine.Instance().onMotionEvent(e4);

        ArrayList<PointF> list = null;
        try {
            Method method = BasePen.class.getDeclaredMethod("getVertexList", null);
            method.setAccessible(true);
            list = (ArrayList<PointF>) method.invoke(p, new Object[]{});
            Assert.assertTrue(list.size() == 1);
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
