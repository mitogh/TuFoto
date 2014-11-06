package mitogh.com.github.tufoto.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class BitmapProcessingUtils {


    public static Bitmap getPortraitAlignment(Bitmap bitmap, int degrees) {
        switch (degrees) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return  BitmapProcessingUtils.rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return BitmapProcessingUtils.rotate(bitmap, -90);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        try {
            Bitmap bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bitmapRotated;
        } catch (OutOfMemoryError e) {
            return bitmap;
        }
    }

    public static Bitmap resize(String imagePath, int width, int height) {

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, bitmapOptions);
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        int scaleFactor = getScaleFactor(width, height, photoW, photoH);

        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;
        bitmapOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);

        return bitmap;
    }

    private static int getScaleFactor(int width, int height, int photoWidth, int photoHeight) {
        try {
            return Math.min(photoWidth / width, photoHeight / height);
        } catch (ArithmeticException e) {
            return 1;
        }
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
