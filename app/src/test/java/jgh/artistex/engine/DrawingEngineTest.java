package jgh.artistex.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.SystemClock;
import android.view.MotionEvent;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import jgh.artistex.BuildConfig;
import jgh.artistex.R;
import jgh.artistex.engine.Layers.Pens.BasePen;
import jgh.artistex.engine.Layers.Pens.Pen;
import jgh.artistex.engine.Layers.Pens.VertexController;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class DrawingEngineTest {

    @Test
    public void test1(){
        DrawingEngine.killEngine();
        DrawingEngine.Instance().setLayer(new Pen());
        DrawingEngine.Instance().setLayer(new Pen());
        DrawingEngine.Instance().setLayer(new Pen());
        DrawingEngine.Instance().setLayer(new Pen());
        Assert.assertTrue(DrawingEngine.Instance().getLayerNameList().size() == 4);
        DrawingEngine.killEngine();
    }

    @Test
    public void test2(){
        DrawingEngine.killEngine();
        Assert.assertTrue(DrawingEngine.Instance().getLayerNameList().size() == 0);
        DrawingEngine.killEngine();
    }


}