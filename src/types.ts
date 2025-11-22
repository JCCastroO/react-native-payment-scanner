import { ViewStyle } from "react-native";

export type BarcodeType =
  | "QR_CODE"
  | "EAN_13"
  | "EAN_8"
  | "CODE_128"
  | "CODE_39"
  | "UPC_A"
  | "UPC_E"
  | "UNKNOWN";

export interface ScanResult {
  data: string;
  type: BarcodeType;
}

export interface ScannerProps {
  onCodeScanned: (result: ScanResult) => void;
  onError?: (error: Error) => void;
  showFrame?: boolean;
  frameColor?: string;
  style?: ViewStyle;
}
