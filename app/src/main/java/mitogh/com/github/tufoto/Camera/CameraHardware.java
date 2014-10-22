package mitogh.com.github.tufoto.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

public class CameraHardware {

    private static final String TAG = CameraHardware.class.getSimpleName();

    public static Integer BACK_CAMERA = null;
    public static Integer FRONTAL_CAMERA = null;

    public CameraHardware() {
        searchCameras();
    }

    private void searchCameras() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camID = 0; camID < cameraCount; camID++) {
            Camera.getCameraInfo(camID, cameraInfo);

            switch (cameraInfo.facing){
                case Camera.CameraInfo.CAMERA_FACING_FRONT:
                    FRONTAL_CAMERA = camID;
                    break;

                case Camera.CameraInfo.CAMERA_FACING_BACK:
                    BACK_CAMERA = camID;
                    break;
            }
        }
    }

    public Camera openFrontalCamera() {
        try {
            return Camera.open(FRONTAL_CAMERA);
        } catch (Exception e) {
            Log.d(TAG, e.getStackTrace().toString());
            return null;
        }
    }

    public Camera openBackCamera() {
        try {
            return Camera.open(BACK_CAMERA);
        } catch (Exception e) {
            Log.d(TAG, e.getStackTrace().toString());
            return null;
        }
    }

    public static boolean exits(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
