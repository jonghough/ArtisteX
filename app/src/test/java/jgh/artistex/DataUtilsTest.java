package jgh.artistex;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import jgh.artistex.BuildConfig;
import jgh.artistex.engine.utils.DataUtils;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class DataUtilsTest {

    @Test
    public void testAlphaString1(){
        int color = 0xFF345678;
        String alpha = DataUtils.getAlpha(color);
        System.out.println("alpha is ******* "+alpha);
        Assert.assertTrue(alpha.equals("ff"));
    }

    @Test
    public void testAlphaString2(){
        int color = 0x23345678;
        String alpha = DataUtils.getAlpha(color);
        System.out.println("alpha is ******* "+alpha);
        Assert.assertTrue(alpha.equals("23"));
    }



    @Test
    public void testAlphaString3(){
        int color = 0x00000000;
        String alpha = DataUtils.getAlpha(color);
        System.out.println("alpha is ******* "+alpha);
        Assert.assertTrue(alpha.equals("00"));
    }

    @Test
    public void testRGBHex1(){
        int color = 0xDCAA2E01;
        String rgb = DataUtils.getRGBHex(color);
        System.out.println("rgb is ******* "+rgb);
        Assert.assertTrue(rgb.toLowerCase().equals("aa2e01"));
    }

    @Test
    public void testRGBHex2(){
        int color = 0xAA000001;
        String rgb = DataUtils.getRGBHex(color);
        System.out.println("rgb is ******* "+rgb);
        Assert.assertTrue(rgb.toLowerCase().equals("000001"));
    }

    @Test
    public void testRGBHex3(){
        int color = 0xAA000000;
        String rgb = DataUtils.getRGBHex(color);
        System.out.println("rgb is ******* "+rgb);
        Assert.assertTrue(rgb.toLowerCase().equals("000000"));
    }
}
