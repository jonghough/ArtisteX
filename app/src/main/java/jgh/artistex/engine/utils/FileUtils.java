package jgh.artistex.engine.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Class holding static methods for files and dealing with internal adn external
 * storage.
 */
public class FileUtils {

    /**
     * Logging Tag
     */
    public static final String TAG = "FileUtils";

    /**
     * Name of root directory for application files.
     */
    public static final String ROOT_DIR = "ArtisteX_Files";

    /**
     * Environment path separator.
     */
    public static final String SPTR = File.separator;

    /**
     * Image directory name.
     */
    public static final String IMAGES = "Images";

    /**
     * Temp directory name.
     */
    public static final String TEMP = "temp";

    /**
     * PNG suffix
     */
    public static final String PNG = ".png";

    /**
     * JPG suffix.
     */
    public static final String JPG = ".jpg";

    /**
     * Prefix for SVG file string.
     */
    public static final String SVG = "SVG";

    /**
     * Header for generated SVG files.
     */
    public static final String SVG_HEADER = "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1 //EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">";

    /**
     * Footer for generated SVG files.
     */
    public static final String SVG_FOOTER = "</svg>";


    /**
     * Returns a unique filename based on the current time (to second accuracy).
     *
     * @param prefix    file name prefix string. If null, no prefix will be prepended.
     * @param extension the file extension.
     * @return Filename including extension.
     */
    public static String getUniqueFilename(String prefix, String extension) {
        String filePrefix = prefix == null ? "" : prefix;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);// 0.. 11
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String filename = "" + year + "_" + getTwoDigitString(month + 1) +
                "_"
                + getTwoDigitString(day) + "_" +
                hour + "_" + minute + "_" + second + "." + extension;

        return filename;
    }


    /**
     * Gets a unique name for a temporary file. File will have <i>tmp</i> extension and
     * be prepended by <i>temp_</i>.
     * @param extension
     * @return a uinque file name for a temporary file.
     */


    public static String getUniqueTempFilename(String extension) {
        return getUniqueFilename("temp_", extension);
    }


    /**
     * Returns a unique svg filename, a filename with .svg exentsion.
     *
     * @return Unique SVG filename
     */
    public static String getSvgFilename() {
        return getUniqueFilename(null, "svg");
    }


    /**
     * Returns a two digit string
     *
     * @param value (1 or two digits, i.e. in range [0,99])
     * @return Two character string represenitng the number.
     */
    private static String getTwoDigitString(int value) {
        String val = "";
        if (value < 10)
            val = "0" + value;
        else
            val = "" + value;

        return val;
    }


    /**
     * Gets the path to the <i>images</i> directory
     *
     * @return path string
     */
    public static String getImagesDirectory() {
        return Environment.getExternalStorageDirectory() + SPTR + ROOT_DIR + SPTR
                + IMAGES + SPTR;
    }


    /**
     * Gets the path to the <i>temp</i> directory
     *
     * @return path string
     */
    public static String getTempDirectory() {
        return Environment.getExternalStorageDirectory() + SPTR + ROOT_DIR + SPTR
                + IMAGES + SPTR + TEMP + SPTR;
    }


    /**
     * Creates the images directory.
     *
     * @return true if success, false otherwise.
     */
    public static boolean makeImagesDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() + SPTR + ROOT_DIR + SPTR
                + IMAGES + SPTR);

        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (!success) {
            Log.d(TAG, "Folder not created.");
        } else {
            Log.d(TAG, "Folder created!");
        }

        return success;
    }

    /**
     * Creates the temp directory. If directory already exists, all files in the directory will be
     * deleted
     *
     * @return true if success, false otherwise.
     */
    public static boolean makeTempDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() + SPTR + ROOT_DIR + SPTR
                + IMAGES + SPTR + TEMP + SPTR);
        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        } else {
            File[] children = folder.listFiles();
            if (children != null && children.length > 0) {
                for (File f : children) {
                    f.delete();
                }
            }
            success = true;
        }

        if (!success) {
            Log.d(TAG, "Folder not created.");
        } else {
            Log.d(TAG, "Folder created!");
        }
        return success;
    }


    /**
     * Gets the user's saved image files, from the <i>images</i> directory.
     *
     * @return List of filenames (the image files)
     */
    public static ArrayList<String> getUserImages() {
        ArrayList<String> filenames = new ArrayList<String>();
        String dir = getImagesDirectory();
        File file = new File(dir);
        for (File f : file.listFiles()) {
            if (!f.isDirectory() && isImageFile(f)) {
                filenames.add(f.getPath());
            }
        }

        return filenames;
    }


    /**
     * Gets the user's saved SVG files, form the <i>images</i> directory.
     *
     * @return List of filenames.
     */
    public static ArrayList<String> getUserSVGFiles() {
        ArrayList<String> filenames = new ArrayList<String>();
        String dir = getImagesDirectory();
        File file = new File(dir);
        for (File f : file.listFiles()) {

            if (!f.isDirectory() && f.getName().endsWith(".svg")) {
                filenames.add(f.getPath());
            }
        }

        return filenames;
    }


    /**
     * Checks if the given file is an image file. Image file means a file with
     * a <code>png</code>, <code>jpg</code>, or <code>jpeg</code> extension.
     *
     * @param file the file to be checked.
     * @return true if image file, false otherwise.
     */
    private static boolean isImageFile(File file) {
        if (file.getName().toLowerCase().endsWith(".png"))
            return true;
        if (file.getName().toLowerCase().endsWith(".jpg"))
            return true;
        if (file.getName().toLowerCase().endsWith(".jpeg"))
            return true;

        return false;
    }


    /**
     * Returns the extension of the file.
     *
     * @param path file path.
     * @return image file extension.
     */
    public static String getImageExtension(String path) {

        if (path.endsWith(".png"))
            return ".png";
        if (path.endsWith(".jpg"))
            return ".jpg";
        if (path.endsWith(".jpeg"))
            return "jpeg";

        return "";
    }



    /**
     * Returns a bitmap from the given file name and absolute path.
     *
     * @param path absolute path to file.
     * @return bitmap or null if not a bitmap file.
     */
    public static Bitmap getBitmapFromUserFile(String path) {

        File file = new File(path);
        if (file.exists()) {
            if (isImageFile(file)) {
                Bitmap bmp = BitmapFactory.decodeFile(path);
                return bmp;
            } else
                return null;
        } else
            return null;
    }


    /**
     * Returns a thumbnail-esque bitmap from the bitmap file at the given location.
     *
     * @param path bitmap file name and absolute path.
     * @return A little bitmap.
     */
    public static Bitmap getThumbnailBitmapFromUserFile(String path) {

        File file = new File(path);
        if (file.exists()) {
            if (isImageFile(file)) {
                Bitmap bmp = BitmapFactory.decodeFile(path);
                bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 10, bmp.getHeight() / 10,
                        false);
                return bmp;
            } else
                return null;
        } else
            return null;
    }


    /**
     * Saves the temp file.
     *
     * @param filename the file name
     * @param contents the file's contents
     * @throws IOException if problem attempting save.
     */
    public static void saveTempFile(String filename, String contents) throws IOException {

        File file = new File(filename);

        try {
            file.createNewFile();
        } catch (IOException e1) {

            throw e1;
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e2) {
            writer.close();
            throw e2;
        }
        try {

            writer.append(contents);
        } catch (IOException e) {
            writer.close();
            throw e;

        }
        try {
            writer.flush();
        } catch (IOException e1) {
            writer.close();
            throw e1;
        }
        try {
            writer.close();
        } catch (IOException e) {
            writer.close();
            throw e;
        }
    }


    /**
     * Returns the contents of a text file.
     *
     * @param filename absolute path to file.
     * @return string contents of file.
     */
    public static String readTextFile(String filename) {
        File file = new File(filename);
        if (!file.exists())
            return null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader(fis));
        String data = "";
        String buffer = "";

        try {
            while ((data = br.readLine()) != null) {
                buffer += data;
            }
        } catch (IOException e) {

            return null;
        }
        try {
            br.close();
        } catch (IOException e) {

        }
        return buffer;
    }
}
