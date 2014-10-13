package mitogh.com.github.tufoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitogh.com.github.tufoto.Image.ProcessImage;


public class ApplyFrames extends ActionBarActivity {

    @InjectView(R.id.imageview_show_picture) ImageView showPictureImageView;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_frames);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra(Main.IMAGE_PATH);

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
        }

        @Override
        public void onBitmapLoaded(Bitmap photo, Picasso.LoadedFrom from) {
            showPictureImageView.setImageBitmap(
                    loadImage()
            );
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
        }
    };

    public Bitmap loadImage() {
        Bitmap bitmap;
        try {
            ProcessImage processImage = new ProcessImage(showPictureImageView, imagePath);
            bitmap = processImage.getBitmap();
        } catch (Exception e) {
            bitmap = null;
        }

        return bitmap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_frames, menu);
        return true;
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
