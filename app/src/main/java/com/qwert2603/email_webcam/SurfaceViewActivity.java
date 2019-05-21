package com.qwert2603.email_webcam;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.Nullable;

public class SurfaceViewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView surfaceView = new SurfaceView(this);
        setContentView(surfaceView);

        try {
            LogUtils.d("getNumberOfCameras == " + Camera.getNumberOfCameras());
            final Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            Camera.Parameters parameters = camera.getParameters();

            int width = 1024;
            int height = 768;

            parameters.setPreviewSize(width, height);
            parameters.setPictureSize(width, height);
            LogUtils.d("size == " + width + " * " + height);

            camera.setParameters(parameters);
            final SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        LogUtils.d("SurfaceViewActivity#surfaceCreated");
                        camera.setPreviewDisplay(surfaceHolder);
                        camera.startPreview();
                        camera.takePicture(null, null, new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera1) {
                                LogUtils.d("SurfaceViewActivity#takePicture & data.length == " + data.length);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                App.Companion.savePhoto(bitmap);
                                camera.stopPreview();
                                camera.release();
                                finish();
                            }
                        });
                    } catch (Exception e) {
                        LogUtils.e("", e);
                        camera.release();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                }
            });
        } catch (Exception e) {
            LogUtils.e("", e);
        }
    }
}
