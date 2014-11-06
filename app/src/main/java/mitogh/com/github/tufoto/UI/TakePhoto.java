package mitogh.com.github.tufoto.ui;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitogh.com.github.tufoto.Config;
import mitogh.com.github.tufoto.R;
import mitogh.com.github.tufoto.camera.CameraHardware;
import mitogh.com.github.tufoto.camera.CameraPreview;
import mitogh.com.github.tufoto.util.DirectoryUtils;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_remove);


        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );
        cameraHardware = new CameraHardware();
        DirectoryUtils.create();
        directoryPath = DirectoryUtils.NAME.getPath();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camera, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_change_camera:
                updateCamera();
                loadCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCamera(){

        releaseCamera();
        setActiveCamera();

        try {
            int rotation = 90;
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRotation(rotation);

            mCamera.setDisplayOrientation(rotation);
            //    mCamera.setParameters(parameters);

            mPreview = new CameraPreview(this, mCamera);
            attachPreview();
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    private void updateCamera(){
        cameraNumber = (cameraNumber + 1) % 2;
    }

    private void setActiveCamera() {
        if(cameraNumber == 1){
            mCamera = cameraHardware.openBackCamera();
        }else{
            mCamera = cameraHardware.openFrontalCamera();
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

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                mCamera.release();
                startFrames(pictureFile.getPath(), cameraNumber);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private void startFrames(String imagePath, int cameraNumber) {
        Intent intent = new Intent(this, ApplyFrames.class);
        intent.putExtra(Config.IMAGE_PATH_ID, imagePath);
        intent.putExtra(Config.CAMERA, cameraNumber);
        startActivity(intent);
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
}