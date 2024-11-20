plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.cst3104.lab05"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cst3104.lab05"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0") {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation("com.google.android.material:material:1.12.0") {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation("androidx.activity:activity:1.8.0") {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation("androidx.constraintlayout:constraintlayout:2.2.0") {
        exclude(group = "com.intellij", module = "annotations")
    }
    implementation("androidx.room:room-runtime:2.6.1") {
        exclude(group = "com.intellij", module = "annotations")
    }
    annotationProcessor("androidx.room:room-compiler:2.6.1") // For ROOM annotation processing with Java

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
