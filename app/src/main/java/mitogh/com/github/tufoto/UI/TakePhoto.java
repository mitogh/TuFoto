package mitogh.com.github.tufoto.UI;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitogh.com.github.tufoto.Camera.Preview;
import mitogh.com.github.tufoto.File.Directory;
import mitogh.com.github.tufoto.File.FileName;
import mitogh.com.github.tufoto.R;

public class TakePhoto extends ActionBarActivity {

    private Camera mCamera;
    private Preview mPreview;

    private static final String TAG = ActionBarActivity.class.getSimpleName();

    @InjectView(R.id.camera_preview) protected FrameLayout preview;
    @InjectView(R.id.button_capture) protected Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        ButterKnife.inject(this);

        mCamera = getCameraInstance();
        mPreview = new Preview(this, mCamera);
        preview.addView(mPreview);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                releaseCamera();
                startFrames(Uri.fromFile(pictureFile).getPath());
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private void startFrames(String imagePath) {
        Intent intent = new Intent(this, ApplyFrames.class);
        intent.putExtra(ApplyFrames.IMAGE_PATH, imagePath);
        startActivity(intent);
    }

    private static File getOutputMediaFile() {
        Directory.create();
        return FileName.create(Directory.NAME.getPath());
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
        return camera;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
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
}
