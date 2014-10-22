package mitogh.com.github.tufoto.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import mitogh.com.github.tufoto.Config;
import mitogh.com.github.tufoto.R;

public class GalleryUtils {

    public static void selectImage(Activity activity) {
        activity.startActivityForResult(
                getImageChooserFrom(activity),
                getIDFromImagesSelectedFromGallery()
        );
    }

    private static Intent getImageChooserFrom(Activity activity){
        return Intent.createChooser(
                getIntentToSelectImage(),
                activity.getString(R.string.label_select_photo_from_library)
        );
    }


    private static Intent getIntentToSelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        return intent;
    }

    public static int getIDFromImagesSelectedFromGallery(){
        return Config.SELECT_PHOTO;
    }

    public static String getImagePath(Context context, Uri uri) {

        if (uri == null) {
            return Config.EMPTY_STRING;
        }

        String[] projection = {
                MediaStore.Images.Media.DATA
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

    public static boolean imageIsWrong(String imagePath) {
        return imagePath.equals(Config.EMPTY_STRING);
    }
}
