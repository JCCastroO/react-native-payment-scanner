package com.barcodescanner;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class BarcodeScannerModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "BarcodeScannerModule";

    public BarcodeScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void requestCameraPermission(Promise promise) {
        try {
            // Lógica de permissão se necessário
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("PERMISSION_ERROR", e.getMessage());
        }
    }
}