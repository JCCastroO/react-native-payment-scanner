# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/24.3.3/tools/proguard/proguard-android.txt

-keep class com.barcodescanner.** { *; }
-keep class com.google.mlkit.** { *; }
```

### 4. **Estrutura final do diretório Android**

Certifique-se que está assim:
```
android/
├── src/
│   └── main/
│       ├── AndroidManifest.xml
│       └── java/
│           └── com/
│               └── barcodescanner/
│                   ├── BarcodeScannerModule.java
│                   ├── BarcodeScannerPackage.java
│                   ├── BarcodeScannerView.java
│                   └── BarcodeScannerViewManager.java
├── build.gradle
└── proguard-rules.pro