package mitogh.com.github.tufoto.ui;

import android.content.Intent;
import android.graphics.Bitmap;
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
    private int rotation_angle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_frames);

        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra(Config.IMAGE_PATH_ID);
        rotation_angle = intent.getIntExtra(Config.ANGLE, ExifInterface.ORIENTATION_NORMAL);
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
        int targetW = displayPhotoImageView.getWidth();
        int targetH = displayPhotoImageView.getHeight();

        Bitmap photoResized = BitmapProcessingUtils.resize(imagePath, targetW, targetH);
        Bitmap photoRotationFixed = BitmapProcessingUtils.rotate(photoResized, rotation_angle);

        return photoRotationFixed;
    }
}
