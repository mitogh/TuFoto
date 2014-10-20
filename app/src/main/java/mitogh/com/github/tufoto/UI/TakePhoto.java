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
import mitogh.com.github.tufoto.Camera.CameraHardware;
import mitogh.com.github.tufoto.Camera.CameraPreview;
import mitogh.com.github.tufoto.File.Directory;
import mitogh.com.github.tufoto.File.FileName;
import mitogh.com.github.tufoto.R;

public class TakePhoto extends ActionBarActivity {

    private Camera mCamera = null;
    private CameraPreview mPreview;
    private String directoryPath;
    private static int cameraNumber = 1;
    private static final String TAG = ActionBarActivity.class.getSimpleName();
    private CameraHardware cameraHardware;

    @InjectView(R.id.camera_preview) protected FrameLayout preview;
    @InjectView(R.id.button_capture) protected Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        ButterKnife.inject(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
        cameraHardware = new CameraHardware();
        Directory.create();
        directoryPath = Directory.NAME.getPath();
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadCamera();
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = FileName.create(directoryPath);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                mCamera.release();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_change_camera:
                changeCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeCamera(){
        cameraNumber = (cameraNumber + 1 ) % 2;
        loadCamera();
    }

    private void loadCamera(){

        releaseCamera();

        if(cameraNumber == 1){
            mCamera = cameraHardware.openBackCamera();
        }else{
            mCamera = cameraHardware.openFrontalCamera();
        }

        try {
            mPreview = new CameraPreview(this, mCamera);
            attachPreview();
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    private void releaseCamera() {
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
        detachPreview();
    }

    private void detachPreview(){
        preview.removeView(mPreview);
    }

    private void attachPreview(){
        preview.addView(mPreview);
    }

}
