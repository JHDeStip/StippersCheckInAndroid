package stip.stipperscheckin.views;

import android.databinding.DataBindingUtil;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

import stannieman.commonservices.models.IHasDataAndSuccessState;
import stannieman.mvvm.ViewBase;
import stip.stipperscheckin.App;
import stip.stipperscheckin.R;
import stip.stipperscheckin.databinding.ScannerViewBinding;
import stip.stipperscheckin.services.IBarcodeImageProcessor;
import stip.stipperscheckin.viewmodels.ScannerViewModel;

public class ScannerView extends ViewBase<ScannerViewModel> implements Camera.PreviewCallback, IBarcodeImageProcessor.IBarcodeImageProcessedCallback {
    private static final int N_BUFFERS = 5;
    private static final int CAMERA_ROTATION = 90;

    private SurfaceHolder previewHolder;

    private Camera camera;
    private Camera.Size previewSize;
    private byte[][] buffers;
    private boolean isProcessing;
    private boolean foundBarcode;
    private boolean surfaceCreated = false;
    private boolean cameraStarted = false;

    public ScannerView() {
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ((ScannerViewBinding) DataBindingUtil.setContentView(this, R.layout.scanner_view)).setViewModel(getViewModel());

        SurfaceView scannerPreview = (SurfaceView) findViewById(R.id.scannerPreview);

        final GestureDetector longPressDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                getViewModel().navigateToSettingsView();
            }
        });

        scannerPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return longPressDetector.onTouchEvent(event);
            }
        });

        previewHolder = scannerPreview.getHolder();
        previewHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surfaceCreated = true;
                startCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                surfaceCreated = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        isProcessing = false;
        foundBarcode = false;

        if (surfaceCreated && !cameraStarted) {
            startCamera();
        }
    }

    @Override
    protected void onPause() {
        stopCamera();
        super.onPause();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (!isProcessing && !foundBarcode) {
            isProcessing = true;
            getViewModel().getBarcodeImageProcessor().processCameraDataAsync(data.clone(), previewSize.width, previewSize.height, this);
        }

        camera.addCallbackBuffer(data);
    }

    @Override
    public void onBarcodeImageProcessed(IHasDataAndSuccessState<Integer> processBarcodeResult) {
        if (processBarcodeResult.isSuccess()) {
            foundBarcode = true;
            stopCamera();
            getViewModel().onScanned(processBarcodeResult.getData());
        }

        isProcessing = false;
    }

    private void startCamera() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = camera.getParameters();
        previewSize = parameters.getPreviewSize();

        setCameraParameters(parameters);

        camera.setDisplayOrientation(CAMERA_ROTATION);
        camera.setParameters(parameters);

        int bufferSize = previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat());
        if (buffers == null) {
            buffers = new byte[N_BUFFERS][bufferSize];
        }

        try {
            camera.setPreviewDisplay(previewHolder);
        } catch (IOException e) {}

        for (byte[] buffer : buffers) {
            camera.addCallbackBuffer(buffer);
        }
        camera.setPreviewCallbackWithBuffer(ScannerView.this);

        camera.startPreview();

        cameraStarted = true;
    }

    private void stopCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            cameraStarted = false;
        }
    }

    private void setCameraParameters(Camera.Parameters parameters) {
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes != null
                && supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)
                && getViewModel().isTorchOn()) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }

        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if (supportedFocusModes != null
                && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        List<String> supportedSceneModes = parameters.getSupportedSceneModes();
        if (supportedSceneModes != null
                && parameters.getSupportedSceneModes().contains(Camera.Parameters.SCENE_MODE_BARCODE)) {
            parameters.setFocusMode(Camera.Parameters.SCENE_MODE_BARCODE);
        }
    }
}
