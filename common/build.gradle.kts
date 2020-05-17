
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    //id("co.touchlab.native.cocoapods")
    //id("kotlinx-serialization")
    id("com.android.library")
    //id("com.squareup.sqldelight")
}

android {
    compileSdkVersion(Versions.compileSdk)
//    defaultConfig {
//        minSdkVersion(Versions.min_sdk)
//        targetSdkVersion(Versions.target_sdk)
//        versionCode = 1
//        versionName = "1.0"
//    }
}