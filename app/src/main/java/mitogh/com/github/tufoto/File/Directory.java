package mitogh.com.github.tufoto.File;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Directory {

    private static final String TAG = Directory.class.getSimpleName();

    private static final File PICTURES = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
    );

    private static final String LABEL = "TuFoto";

    public static final File NAME = new File(PICTURES, LABEL);


    public static void create(){
        if( !exists() ){
            if( !createDirectory() ){
                Log.d(TAG, "failed to create directory");
            }
        }
    }

    public static boolean exists(){
        return Directory.NAME.exists();
    }

    private static boolean createDirectory(){
        return Directory.NAME.mkdirs();
    }
}
