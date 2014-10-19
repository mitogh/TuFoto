package mitogh.com.github.tufoto.UI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitogh.com.github.tufoto.R;


public class Main extends ActionBarActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 100;
    public static final String IMAGE_PATH = "IMAGE_PATH";
    private final String EMPTY_STRING = "";

    @InjectView(R.id.button_select_picture)
    Button selectPictureButton;
    @InjectView(R.id.button_take_picture)
    Button takePictureButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ButterKnife.inject(this);

        if(checkCameraHardware(this) == false){
            takePictureButton.setVisibility(View.INVISIBLE);
        }

        takePictureButton.setOnClickListener(this);
        selectPictureButton.setOnClickListener(this);
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_take_picture:
                openTheCamera();
                break;

            case R.id.button_select_picture:
                selecImageFromLibrary();
                break;
        }
    }

    private void openTheCamera(){
        Intent intent = new Intent(this, TakePhoto.class);
        startActivity(intent);
    }

    private void selecImageFromLibrary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, getString(R.string.label_select_picture_from_library)),
                SELECT_PICTURE
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String selectedImagePath = "";

        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
        }

        if(selectedImagePath.equals(EMPTY_STRING)){
            Toast.makeText(this, getString(R.string.message_problem_retrieving_image), Toast.LENGTH_LONG)
                    .show();
        }else{
            Intent intent = new Intent(this, ApplyFrames.class);
            intent.putExtra(IMAGE_PATH, selectedImagePath);
            startActivity(intent);
        }
    }

    public String getPath(Uri uri) {

        if( uri == null ) {
            return EMPTY_STRING;
        }

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
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
}