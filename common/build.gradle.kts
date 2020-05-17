plugins {
    kotlin("multiplatform")
    //id("co.touchlab.native.cocoapods")
    //id("kotlinx-serialization")
    id("com.android.library")
    //id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(Versions.compileSdk)
    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = 1
        versionName = "1.0"
    }
}