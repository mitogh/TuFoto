package mitogh.com.github.tufoto.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import mitogh.com.github.tufoto.Bitmap.Processing;
import mitogh.com.github.tufoto.R;


public class ApplyFrames extends ActionBarActivity {

    public static final String TAG = ActionBarActivity.class.getSimpleName();
    public static final String IMAGE_PATH = "IMAGE_PATH";

    @InjectView(R.id.imageview_display_photo)
    ImageView displayPhotoImageView;

    @InjectView(R.id.progressbar_image_loading)
    ProgressBar imageLoadingProgressBar;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_frames);

        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra(ApplyFrames.IMAGE_PATH);

        Picasso.with(
                getApplicationContext()
        ).load(
                new File(imagePath)
        ).into(
                callBackToProcessImage
        );
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

    public Bitmap loadImage() {
        int targetW = displayPhotoImageView.getWidth();
        int targetH = displayPhotoImageView.getHeight();

        Bitmap bitmap = Processing.resize(imagePath, targetW, targetH);

        try {
            int orientation = Processing.getOrientation(imagePath);
            bitmap = Processing.fixOrientation(bitmap, orientation);
        } catch (IOException e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
        return bitmap;
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
                Intent intent = new Intent(this, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
