package com.barcodescanner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class BarcodeScannerView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera camera;
    private BarcodeScanner barcodeScanner;
    private boolean isProcessing = false;
    private static final String TAG = "BarcodeScannerView";

    public BarcodeScannerView(Context context) {
        super(context);
        init();
    }

    public BarcodeScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        barcodeScanner = BarcodeScanning.getClient();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.setPreviewCallback(this);
        } catch (IOException e) {
            Log.e(TAG, "Error setting camera preview", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            camera.setParameters(params);
            camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isProcessing) return;
        
        isProcessing = true;
        
        try {
            Camera.Parameters params = camera.getParameters();
            Camera.Size size = params.getPreviewSize();
            
            InputImage image = InputImage.fromByteArray(
                data,
                size.width,
                size.height,
                0,
                InputImage.IMAGE_FORMAT_NV21
            );

            barcodeScanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        onBarcodeDetected(barcode);
                        break;
                    }
                    isProcessing = false;
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Barcode scanning failed", e);
                    isProcessing = false;
                });
        } catch (Exception e) {
            Log.e(TAG, "Error processing frame", e);
            isProcessing = false;
        }
    }

    public void cleanup() {
    if (camera != null) {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}

    private void onBarcodeDetected(Barcode barcode) {
        WritableMap event = Arguments.createMap();
        event.putString("data", barcode.getRawValue());
        event.putString("type", getBarcodeType(barcode.getFormat()));
        
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class)
            .receiveEvent(getId(), "onCodeScanned", event);
    }

    private String getBarcodeType(int format) {
        switch (format) {
            case Barcode.FORMAT_QR_CODE: return "QR_CODE";
            case Barcode.FORMAT_EAN_13: return "EAN_13";
            case Barcode.FORMAT_EAN_8: return "EAN_8";
            case Barcode.FORMAT_CODE_128: return "CODE_128";
            case Barcode.FORMAT_CODE_39: return "CODE_39";
            case Barcode.FORMAT_UPC_A: return "UPC_A";
            case Barcode.FORMAT_UPC_E: return "UPC_E";
            default: return "UNKNOWN";
        }
    }
}