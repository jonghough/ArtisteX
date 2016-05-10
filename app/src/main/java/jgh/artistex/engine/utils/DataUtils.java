package jgh.artistex.engine.utils;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * Some static functions for handling data, encodings and strings.
 */
public class DataUtils {

    /**
     * Base 64 encodes the given bitmap. The compression format is given by
     * <code>format</code>.
     *
     * @param image  the bitmap.
     * @param format compression format
     * @return String representation of base64 encoding of bitmap
     */
    public static String encodeTobase64(Bitmap image, Bitmap.CompressFormat format) {
        if (image == null)
            throw new IllegalArgumentException("Cannot pass null bitmap to encoding method.");
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(format, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }


    /**
     * Decodes the string into a bitmap. String should be a base64 encoding of a
     * bitmap.
     *
     * @param input string
     * @return a bitmap
     */
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    /**
     * Gets the RGB values from the color int (e.g. 32-bit integer represenitng ARGB
     * color value.) THe reuslt is returned as a hex string.
     *
     * @param ARGBcolor some color int
     * @return hex string represenitng RGB values. (the last 3 bytes)
     */
    public static String getRGBHex(int ARGBcolor) {
        String s = Integer.toHexString(ARGBcolor);
        char[] c = s.toCharArray();
        // if argb
        if (c.length == 8) {
            String r = s.substring(2);// remove alpha
            return r;
        } else if (c.length == 6) {//only rgb
            return "000000";
        } else
            return "FFFFFF";// default, white
    }


    /**
     * Gets the alpha value from a color int, i.e. a 32-bit
     * integer representing ARGB colors. So we get the first byte.
     *
     * @param ARGBcolor color represented as int.
     * @return hex string representing the first byte of the int (i.e. the alpha byte).
     */
    public static String getAlpha(int ARGBcolor) {
        if(ARGBcolor == 0)
            return "00";
        String s = Integer.toHexString(ARGBcolor);
        char[] c = s.toCharArray();
        // if argb
        if (c.length == 8) {
            String r = s.substring(0, 2);// remove rgb
            return r;
        } else if (c.length == 6) {//only rgb
            return "00";
        } else
            return "FF";// default, white
    }

    /**
     * Gets a decimal representation of the alpha value. i.e. alpha range is [0,256], so
     * this method returns a float in range [0,1] where 1 is equivalent to 256.
     *
     * @param alphaString alpha string representation (hex)
     * @return floating point between 0 and 1.
     */
    public static float getAlphaDecimal(String alphaString) {
        int alpha = 0;
        try {
            alpha = Integer.valueOf(alphaString, 16);
            float alphaFloat = ((float) alpha) / 256;
        } catch (NumberFormatException e) {
            return 0;
        }
        return alpha;
    }

    /**
     * Sends a mail with attached image or file.
     * @param activity
     * @param subj
     * @param mesg
     * @param recipient
     * @param uri
     */
    public static void sendMail(Activity activity, String subj, String mesg, String recipient,
                                ArrayList<Uri> uri) {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        String[] recipients = new String[] {
                recipient, "",
        };
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subj);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mesg);

        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri);

        emailIntent.setType("image/jpeg");
        activity.startActivity(Intent.createChooser(emailIntent, "Send Email"));

    }
}
