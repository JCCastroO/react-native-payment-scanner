package com.barcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class BarcodeScannerViewManager extends SimpleViewManager<BarcodeScannerView> {
    public static final String REACT_CLASS = "BarcodeScannerView";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected BarcodeScannerView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new BarcodeScannerView(reactContext);
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
            .put("onCodeScanned", MapBuilder.of("registrationName", "onCodeScanned"))
            .put("onError", MapBuilder.of("registrationName", "onError"))
            .build();
    }

    @ReactProp(name = "showFrame")
    public void setShowFrame(BarcodeScannerView view, boolean showFrame) {
        // Você pode implementar lógica adicional se quiser controlar o frame nativamente
    }

    @ReactProp(name = "frameColor")
    public void setFrameColor(BarcodeScannerView view, @Nullable String frameColor) {
        // Você pode implementar lógica adicional se quiser controlar a cor do frame nativamente
    }

    @Override
    public void onDropViewInstance(@NonNull BarcodeScannerView view) {
        super.onDropViewInstance(view);
        view.cleanup();
    }
}