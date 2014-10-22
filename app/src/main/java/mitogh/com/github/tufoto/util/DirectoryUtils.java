package mitogh.com.github.tufoto.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DirectoryUtils {

    private static final String TAG = DirectoryUtils.class.getSimpleName();

    private static final File PICTURES = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
    );

    private static final String LABEL = "TuFoto";

    public static final File NAME = new File(PICTURES, LABEL);


    public static void create(){
        if( !exists() ){
            if( !createDirectory() ){
                Log.d(TAG, "failed to directory");
            }
        }
    }

    public static boolean exists(){
        return DirectoryUtils.NAME.exists();
    }

    private static boolean createDirectory(){
        return DirectoryUtils.NAME.mkdirs();
    }
}
