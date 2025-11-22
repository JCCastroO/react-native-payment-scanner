import React, { useEffect } from "react";
import {
  requireNativeComponent,
  NativeModules,
  StyleSheet,
  View,
  Platform,
  PermissionsAndroid,
  ViewProps,
} from "react-native";
import { ScannerProps, ScanResult } from "./types";

interface NativeScannerProps extends ViewProps {
  onCodeScanned?: (event: { nativeEvent: ScanResult }) => void;
  onError?: (event: { nativeEvent: { message: string } }) => void;
  showFrame?: boolean;
  frameColor?: string;
}

const NativeBarcodeScannerView =
  requireNativeComponent<NativeScannerProps>("BarcodeScannerView");
const { BarcodeScannerModule } = NativeModules;

export const BarcodeScanner: React.FC<ScannerProps> = ({
  onCodeScanned,
  onError,
  showFrame = true,
  frameColor = "#00FF00",
  style,
}) => {
  useEffect(() => {
    requestPermissions();
  }, []);

  const requestPermissions = async () => {
    try {
      if (Platform.OS === "android") {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.CAMERA,
          {
            title: "Permissão de Câmera",
            message: "O app precisa de acesso à câmera para escanear códigos",
            buttonNeutral: "Perguntar Depois",
            buttonNegative: "Cancelar",
            buttonPositive: "OK",
          }
        );

        if (granted !== PermissionsAndroid.RESULTS.GRANTED) {
          onError?.(new Error("Permissão de câmera negada"));
        }
      } else {
        const result = await BarcodeScannerModule.requestCameraPermission();
        if (!result) {
          onError?.(new Error("Permissão de câmera negada"));
        }
      }
    } catch (error) {
      onError?.(error as Error);
    }
  };

  const handleCodeScanned = (event: { nativeEvent: ScanResult }) => {
    onCodeScanned(event.nativeEvent);
  };

  const handleError = (event: { nativeEvent: { message: string } }) => {
    onError?.(new Error(event.nativeEvent.message));
  };

  return (
    <View style={[styles.container, style]}>
      <NativeBarcodeScannerView
        style={StyleSheet.absoluteFill}
        onCodeScanned={handleCodeScanned}
        onError={handleError}
        showFrame={false} // Controlamos o frame no React Native
        frameColor={frameColor}
      />
      {showFrame && (
        <View style={styles.frameContainer}>
          <View style={[styles.frame, { borderColor: frameColor }]} />
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "black",
  },
  frameContainer: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: "center",
    alignItems: "center",
  },
  frame: {
    width: 250,
    height: 250,
    borderWidth: 3,
    borderRadius: 12,
    backgroundColor: "transparent",
  },
});
