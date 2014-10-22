package mitogh.com.github.tufoto.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

        ButterKnife.inject(this);

        disableTakePhotoButtonIf(!CameraHardware.exits(this));

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCameraActivity() {
        startActivity(
                new Intent(this, TakePhoto.class)
        );
    }

    private void disableTakePhotoButtonIf(Boolean condition) {
        if (condition) {
            takePhotoButton.setVisibility(View.INVISIBLE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String selectedImagePath = Config.EMPTY_STRING;

        if (resultCode == RESULT_OK && requestCode == GalleryUtils.getIDFromImagesSelectedFromGallery() ) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = GalleryUtils.getImagePath(this, selectedImageUri);
        }

        if (GalleryUtils.imageIsWrong(selectedImagePath)) {
            showErrorMessage();
        } else {
            startApplyFramesWith(selectedImagePath);
        }
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
