def safeExtGet(prop, fallback) {
    rootProject.ext.has(prop) ? rootProject.ext.get(prop) : fallback
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
    }
}

apply plugin: 'com.android.library'

android {
    namespace "com.barcodescanner"
    compileSdkVersion safeExtGet('compileSdkVersion', 34)
    buildToolsVersion safeExtGet('buildToolsVersion', '34.0.0')

    defaultConfig {
        minSdkVersion safeExtGet('minSdkVersion', 21)
        targetSdkVersion safeExtGet('targetSdkVersion', 34)
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    lintOptions {
        abortOnError false
    }
}

repositories {
    google()
    mavenCentral()
    maven {
        url "https://maven.google.com"
    }
}

dependencies {
    // React Native
    implementation 'com.facebook.react:react-native:+'
    
    // ML Kit - Barcode Scanning
    implementation 'com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0'
}