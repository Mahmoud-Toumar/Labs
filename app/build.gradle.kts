plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.cst3104.lab6"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cst3104.lab6"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.material) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.activity) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.constraintlayout) {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation(libs.room.runtime) {
        exclude(group = "com.intellij", module = "annotations")
    }
    annotationProcessor(libs.room.compiler) // Use annotationProcessor for ROOM compiler with Java

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
