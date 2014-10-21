package mitogh.com.github.tufoto.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class BitmapProcessingUtils {

    private static final String TAG = BitmapProcessingUtils.class.getSimpleName();

    public static int getOrientation(String imagePath) throws IOException {
        ExifInterface ei = new ExifInterface(imagePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

        return orientation;
    }

    public static Bitmap fixOrientation(Bitmap source, int orientation){
        return rotate(source, orientation);
    }

    public static int getImageOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static Bitmap rotate(Bitmap bitmap, int orientation){

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    Log.d(TAG, "Orientation is NORMAL");
                    return bitmap;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    Log.d(TAG, "Orientation is FLIP HORIZONTAL");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    Log.d(TAG, "Orientation is ROTATE 180");
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    Log.d(TAG, "Orientation is FLIP VERTICAL");
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    Log.d(TAG, "Orientation is TRANSPOSE");
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    Log.d(TAG, "Orientation is ROTATE 90");
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    Log.d(TAG, "Orientation is TRANSVERSE");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    Log.d(TAG, "Orientation is ROTATE 270");
                    break;
                default:
                    return bitmap;
            }
            try {
                Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return bmRotated;
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
    }

    public static Bitmap resize(String imagePath, int width, int height) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bitmapOptions);
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor;
        try {
            scaleFactor = Math.min(photoW / width, photoH / height);
        } catch( ArithmeticException e){
            scaleFactor = 1;
        }

        // Decode the image file into a Bitmap sized to fill the View
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;
        bitmapOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);

        return bitmap;
    }

    public Bitmap combineImages(Bitmap frame, Bitmap image) {

        Bitmap cs = null;
        Bitmap rs = null;

        rs = Bitmap.createScaledBitmap(frame, image.getWidth(),
                image.getHeight(), true);

        cs = Bitmap.createBitmap(rs.getWidth(), rs.getHeight(),
                Bitmap.Config.RGB_565);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(image, 0, 0, null);
        comboImage.drawBitmap(rs, 0, 0, null);

        if (rs != null) {
            rs.recycle();
            rs = null;
        }
        Runtime.getRuntime().gc();

        return cs;
    }
}
