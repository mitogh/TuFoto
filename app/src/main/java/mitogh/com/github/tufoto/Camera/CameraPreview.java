package mitogh.com.github.tufoto.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    private static String TAG = CameraPreview.class.getSimpleName();

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if (Build.VERSION.SDK_INT >= 8) {
                mCamera.setDisplayOrientation(90);
            }
        }catch(Exception e){
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.getHolder().removeCallback(this);

        try {
            mCamera.stopPreview();
        }catch(Exception e){
            Log.d(TAG, e.getStackTrace().toString());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        // preview surface does not exist
        if (mHolder.getSurface() == null) {
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG, e.getStackTrace().toString());
        }

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
