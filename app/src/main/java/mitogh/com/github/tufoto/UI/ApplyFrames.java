package mitogh.com.github.tufoto.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitogh.com.github.tufoto.Config;
import mitogh.com.github.tufoto.R;
import mitogh.com.github.tufoto.util.BitmapProcessingUtils;


public class ApplyFrames extends ActionBarActivity {

    public static final String TAG = ActionBarActivity.class.getSimpleName();

    @InjectView(R.id.imageview_display_photo)
    ImageView displayPhotoImageView;

    @InjectView(R.id.progressbar_image_loading)
    ProgressBar imageLoadingProgressBar;

    private String imagePath;
    private int cameraNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_frames);

        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra(Config.IMAGE_PATH_ID);
        cameraNumber = intent.getIntExtra(Config.CAMERA, 1);
        Log.d("DEBUG", imagePath);
        Picasso.with(
                getApplicationContext()
        ).load(
                new File(imagePath)
        ).into(
                callBackToProcessImage
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_frames, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Target callBackToProcessImage = new Target() {
        @Override
        public void onPrepareLoad(Drawable drawable) {
            imageLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onBitmapLoaded(Bitmap photo, Picasso.LoadedFrom from) {
            displayPhotoImageView.setImageBitmap(
                    loadImage()
            );
            imageLoadingProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
        }
    };

    public Bitmap loadImage(){

        Bitmap realImage;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;

        options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

        options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future

        int targetW = displayPhotoImageView.getWidth();
        int targetH = displayPhotoImageView.getHeight();

        realImage = BitmapProcessingUtils.resize(imagePath, targetW, targetH);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            Log.d("EXIF value -> ",
                    exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("1")) {
                if(cameraNumber == 1) {
                    realImage = rotate(realImage, 90);
                }else {
                    realImage = rotate(realImage, 270);
                }
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("8")) {
                realImage = rotate(realImage, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("3")) {
                realImage = rotate(realImage, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("0")) {
                if(cameraNumber == 0){
                    realImage = rotate(realImage, 180);
                }
                realImage = rotate(realImage, 90);
            } else if((exif.getAttribute(ExifInterface.TAG_ORIENTATION)
                    .equalsIgnoreCase("6")) ){
            }
        } catch (Exception e) {

        }

        return realImage;
    }

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, false);
    }
}
