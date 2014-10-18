package mitogh.com.github.tufoto;

import android.content.Intent;
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

        ButterKnife.inject(this);

        takePictureButton.setOnClickListener(this);
        selectPictureButton.setOnClickListener(this);
    }


    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_take_picture:
                break;

            case R.id.button_select_picture:
                selecImageFromLibrary();
                break;
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
