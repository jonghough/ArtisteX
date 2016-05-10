package jgh.artistex;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import jgh.artistex.BuildConfig;
import jgh.artistex.engine.utils.ColorPicker;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class UtilTest{

    @Test
    public void testARGB1(){
        int color = 0xABCDEF01;
        int alpha = ColorPicker.getAlpha(color);
        int red = ColorPicker.getRed(color);
        int blue = ColorPicker.getBlue(color);
        int green = ColorPicker.getGreen(color);

        Assert.assertTrue(alpha == 0xAB && red == 0xCD && blue == 0xEF && green == 0x01);
    }

    @Test
    public void testARGB2(){
        int color = 0xFF0000EE;
        int alpha = ColorPicker.getAlpha(color);
        int red = ColorPicker.getRed(color);
        int blue = ColorPicker.getBlue(color);
        int green = ColorPicker.getGreen(color);

        Assert.assertTrue(alpha == 0xFF && red == 0x00 && blue == 0x00 && green == 0xEE);
    }

    @Test
    public void testARGB3(){
        int color = 0x13;
        int alpha = ColorPicker.getAlpha(color);
        int red = ColorPicker.getRed(color);
        int blue = ColorPicker.getBlue(color);
        int green = ColorPicker.getGreen(color);

        Assert.assertTrue(alpha == 0x00 && red == 0x00 && blue == 0x00 && green == 0x13);
    }

    @Test
    public void testSetAlpha1(){
        int color = 0x00000000;
        int nextColor = ColorPicker.setAlpha(color, 0xFF);

        Assert.assertTrue(nextColor == 0xFF000000);
    }

    @Test
    public void testSetRed1(){
        int color = 0x00000000;
        int nextColor = ColorPicker.setRed(color, 0x88);

        Assert.assertTrue(nextColor == 0x00880000);
    }

    @Test
    public void testSetGreen1(){
        int color = 0x00000000;
        int nextColor = ColorPicker.setGreen(color, 0xAD);

        Assert.assertTrue(nextColor == 0x0000AD00);
    }

    @Test
    public void testSetBlue1(){
        int color = 0x00000000;
        int nextColor = ColorPicker.setBlue(color, 0x3E);

        Assert.assertTrue(nextColor == 0x0000003E);
    }
}