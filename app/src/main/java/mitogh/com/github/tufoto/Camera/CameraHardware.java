package mitogh.com.github.tufoto.Camera;

import android.content.Context;
import android.content.pm.PackageManager;

public class CameraHardware {

    public static boolean exits(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
