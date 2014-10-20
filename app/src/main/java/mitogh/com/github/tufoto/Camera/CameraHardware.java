package mitogh.com.github.tufoto.Camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

public class CameraHardware {

    private Camera camera = null;
    private static final String TAG = CameraHardware.class.getSimpleName();

    private static Integer BACK_CAMERA = null;
    private static Integer FRONTAL_CAMERA = null;

    private Boolean isCameraOpen = false;

    public CameraHardware() {
        searchCameras();
    }

    private Camera searchCameras() {
        int cameraCount = 0;
        Camera cam = null;
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
        return cam;
    }

    public void openFrontalCamera() {
        try {
            if(!isCameraOpen) {
                camera = Camera.open(FRONTAL_CAMERA);
                isCameraOpen = true;
            }else{
                closeCamera();
            }
        } catch (Exception e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
    }

    public void openBackCamera() {
        try {
            if(!isCameraOpen) {
                camera = Camera.open(BACK_CAMERA);
                isCameraOpen = true;
            }else{
                closeCamera();
            }
        } catch (Exception e) {
            Log.d(TAG, e.getStackTrace().toString());
        }
    }

    private void closeCamera() {
        camera = null;
        isCameraOpen = false;
        relase();
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
