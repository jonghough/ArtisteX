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

import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import jgh.artistex.BuildConfig;
import jgh.artistex.R;
import jgh.artistex.engine.Layers.Pens.PolygonFactory;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class PolygonFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void polygonFactoryTest1() {
        PolygonFactory.createPolygon(0/* 0 vertices */, 0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void polygonFactoryTest2() {
        PolygonFactory.createPolygon(-10 /* -10 vertices */, 0, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void polygonFactoryTest3() {
        PolygonFactory.createPolygon(10, 0, 0, 0 /* 0 radius */);
    }

    @Test(expected = IllegalArgumentException.class)
    public void polygonFactoryTest4() {
        PolygonFactory.createPolygon(10, 0, 0, -5 /* negative radius */);
    }

    @Test
    public void polygonFactoryTest5(){
        ArrayList<PointF> points = PolygonFactory.createPolygon(10, 0, 0, 100);
        Assert.assertEquals(10, points.size());
    }

    @Test
    public void polygonFactoryTest6(){
        ArrayList<PointF> points = PolygonFactory.createShapeStar(10, 0, 0, 100);
        Assert.assertEquals(20, points.size());
    }

}