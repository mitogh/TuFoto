package mitogh.com.github.tufoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Main extends ActionBarActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static int LOAD_IMAGE_RESULTS = 1;


    static final String FILE_PATH = "FILE_PATH";

    private String mCurrentPhotoPath;

    java.io.File lastSavedFile;

    @InjectView(R.id.button_take_picture) Button takePicture;

    @InjectView(R.id.imageview_show_picture) ImageView showPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        takePicture.setOnClickListener(this);
    }



    public void onClick(View v){
                dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            try {
                lastSavedFile = new ImageFile().getImageFile();
                mCurrentPhotoPath = lastSavedFile.getAbsolutePath();
            } catch (IOException ex) {
            }

            if (lastSavedFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(lastSavedFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                setResult(RESULT_OK, takePictureIntent);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_IMAGE_CAPTURE:
                    //galleryAddPic();
                    setPic();
                    break;
            }
        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        java.io.File f = new java.io.File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private Target target = new Target() {
        @Override
        public void onPrepareLoad(Drawable drawable){
        }

        @Override
        public void onBitmapLoaded(Bitmap photo, Picasso.LoadedFrom from){
            showPicture.setImageBitmap(
                    loadImage()
            );

            Log.d("ImageView Width:", String.valueOf(showPicture.getHeight()));
        }

        @Override
        public void onBitmapFailed(Drawable arg0)
        {
        }
    };



    private void setPic() {
        java.io.File tmp = new java.io.File(mCurrentPhotoPath);
        Picasso.with(this).load(tmp).into(target);
    }

    public Bitmap loadImage() {
            int targetW = showPicture.getWidth();
            int targetH = showPicture.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

        Bitmap bitmap =  BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        try{
            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = RotateBitmap(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = RotateBitmap(bitmap, 180);
                    break;
                // etc.
            }
        }catch( Exception e){


        }

        Log.d("Bitmap width: ", String.valueOf(bitmap.getWidth()));
        Log.d("Bitmap height: ", String.valueOf(bitmap.getHeight()));

        return bitmap;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(FILE_PATH, mCurrentPhotoPath);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPhotoPath = savedInstanceState.getString(FILE_PATH);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
