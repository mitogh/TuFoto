package mitogh.com.github.tufoto.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitogh.com.github.tufoto.Config;
import mitogh.com.github.tufoto.R;
import mitogh.com.github.tufoto.camera.CameraHardware;
import mitogh.com.github.tufoto.util.GalleryUtils;
import mitogh.com.github.tufoto.util.MessageUtils;

public class Main extends ActionBarActivity{

    @InjectView(R.id.button_select_photo) Button selectPhotoButton;
    @InjectView(R.id.button_open_camera) Button takePhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        ButterKnife.inject(Main.this);

        disableTakePhotoButtonIf(
                !CameraHardware.exits(
                        getApplicationContext()
                )
        );

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraActivity();
            }
        });
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryUtils.selectImage(Main.this);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void openCameraActivity() {
        startActivity(
                new Intent(getApplicationContext(), TakePhoto.class)
        );
    }

    private void disableTakePhotoButtonIf(Boolean condition) {
        takePhotoButton.setVisibility(
                (condition) ? View.INVISIBLE : View.VISIBLE
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String selectedImagePath = Config.EMPTY_STRING;

        if (successfulRequest(requestCode, resultCode)) {
            selectedImagePath = getImagePath(data);
        }

        if(selectedImagePath == null ){
            Log.d("DEBUG", "selectedImagePath is null");
        }else {
            if (GalleryUtils.imageIsWrong(selectedImagePath)) {
                showErrorMessage();
            } else {
                startApplyFramesWith(selectedImagePath);
            }
        }
    }

    private Uri getImageUri(Intent data){
        return data.getData();
    }

    private String getImagePath(Intent data){
        Uri selectedImageUri = getImageUri(data);
        return GalleryUtils.getImagePath(this, selectedImageUri);
    }

    private boolean successfulRequest(int requestCode, int resultCode) {
        return isSuccess(resultCode) && isFromGallery(requestCode);
    }

    private boolean isSuccess(int resultCode) {
        return resultCode == RESULT_OK;
    }

    private boolean isFromGallery(int requestCode) {
        return requestCode == GalleryUtils.getIDFromImagesSelectedFromGallery();
    }

    private void showErrorMessage() {
        MessageUtils.create(
                getApplicationContext(), getString(R.string.message_problem_retrieving_image)
        ).show();
    }

    private void startApplyFramesWith(String selectedImagePath) {
        Intent intent = new Intent(this, ApplyFrames.class);
        intent.putExtra(Config.IMAGE_PATH_ID, selectedImagePath);
        startActivity(intent);
    }
}
