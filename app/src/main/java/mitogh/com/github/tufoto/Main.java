package mitogh.com.github.tufoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import mitogh.com.github.tufoto.Image.ProcessImage;


public class Main extends ActionBarActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static int LOAD_IMAGE_RESULTS = 1;


    static final String FILE_PATH = "FILE_PATH";

    private String mCurrentPhotoPath;

    java.io.File lastSavedFile;

    @InjectView(R.id.button_select_picture) Button selectPictureButton;
    @InjectView(R.id.button_take_picture) Button takePictureButton;

    @InjectView(R.id.imageview_show_picture) ImageView showPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        takePictureButton.setOnClickListener(this);
        selectPictureButton.setOnClickListener(this);
    }


    public void onClick(View v) {

        int id = v.getId();
        switch(id){
            case R.id.button_take_picture:
                dispatchTakePictureIntent();
                break;

            case R.id.button_select_picture:
                Intent intent = new Intent(this, AddFrames.class);
                startActivity(intent);
                break;
        }
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
        public void onPrepareLoad(Drawable drawable) {
        }

        @Override
        public void onBitmapLoaded(Bitmap photo, Picasso.LoadedFrom from) {
            showPicture.setImageBitmap(
                    loadImage()
            );

            Log.d("ImageView Width:", String.valueOf(showPicture.getHeight()));
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
        }
    };


    private void setPic() {
        java.io.File tmp = new java.io.File(mCurrentPhotoPath);
        Picasso.with(this).load(tmp).into(target);
    }

    public Bitmap loadImage() {
        Bitmap bitmap;
        try {
            ProcessImage processImage = new ProcessImage(showPicture, mCurrentPhotoPath);
            bitmap = processImage.getBitmap();
        } catch (Exception e) {
            bitmap = null;
        }

        return bitmap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
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
