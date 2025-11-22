"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.BarcodeScanner = void 0;
const react_1 = __importStar(require("react"));
const react_native_1 = require("react-native");
const NativeBarcodeScannerView = (0, react_native_1.requireNativeComponent)("BarcodeScannerView");
const { BarcodeScannerModule } = react_native_1.NativeModules;
const BarcodeScanner = ({ onCodeScanned, onError, showFrame = true, frameColor = "#00FF00", style, }) => {
    (0, react_1.useEffect)(() => {
        requestPermissions();
    }, []);
    const requestPermissions = async () => {
        try {
            if (react_native_1.Platform.OS === "android") {
                const granted = await react_native_1.PermissionsAndroid.request(react_native_1.PermissionsAndroid.PERMISSIONS.CAMERA, {
                    title: "Permissão de Câmera",
                    message: "O app precisa de acesso à câmera para escanear códigos",
                    buttonNeutral: "Perguntar Depois",
                    buttonNegative: "Cancelar",
                    buttonPositive: "OK",
                });
                if (granted !== react_native_1.PermissionsAndroid.RESULTS.GRANTED) {
                    onError?.(new Error("Permissão de câmera negada"));
                }
            }
            else {
                const result = await BarcodeScannerModule.requestCameraPermission();
                if (!result) {
                    onError?.(new Error("Permissão de câmera negada"));
                }
            }
        }
        catch (error) {
            onError?.(error);
        }
    };
    const handleCodeScanned = (event) => {
        onCodeScanned(event.nativeEvent);
    };
    const handleError = (event) => {
        onError?.(new Error(event.nativeEvent.message));
    };
    return (react_1.default.createElement(react_native_1.View, { style: [styles.container, style] },
        react_1.default.createElement(NativeBarcodeScannerView, { style: react_native_1.StyleSheet.absoluteFill, onCodeScanned: handleCodeScanned, onError: handleError, showFrame: false, frameColor: frameColor }),
        showFrame && (react_1.default.createElement(react_native_1.View, { style: styles.frameContainer },
            react_1.default.createElement(react_native_1.View, { style: [styles.frame, { borderColor: frameColor }] })))));
};
exports.BarcodeScanner = BarcodeScanner;
const styles = react_native_1.StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "black",
    },
    frameContainer: {
        ...react_native_1.StyleSheet.absoluteFillObject,
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
