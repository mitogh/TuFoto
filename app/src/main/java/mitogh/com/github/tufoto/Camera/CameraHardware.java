package mitogh.com.github.tufoto.Camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

public class CameraHardware {

    private Camera camera = null;

    private static final String TAG = CameraHardware.class.getSimpleName();

    public CameraHardware() {
        openCamera();
    }

    private void openCamera() {
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
    }

    public Camera relase(){
        if (camera != null) {
            camera.release();
        }

        return null;
    }

    public Camera getCamera() {
        return camera;
    }

    public static boolean exits(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
