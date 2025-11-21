plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.ybl"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ybl"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation (libs.retrofit)
    implementation (libs.com.squareup.retrofit2.converter.gson10)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)
    implementation (libs.gson)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.mapbox.maps:android:10.16.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.mapbox.mapboxsdk:mapbox-sdk-geojson:5.9.0")
    implementation ("com.mapbox.mapboxsdk:mapbox-sdk-turf:5.9.0")
    implementation ("com.mapbox.navigation:android:2.15.1")
    implementation("com.mapbox.plugin:maps-annotation:10.16.0")
    implementation("com.mapbox.plugin:maps-locationcomponent:10.16.0")

    implementation("com.google.android.gms:play-services-maps:18.2.0")

    implementation (libs.swiperefreshlayout)



}