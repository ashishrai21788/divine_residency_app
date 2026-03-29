plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.app.divine"
    compileSdk = 35
    packaging {
        dex {
            useLegacyPackaging = false
        }
    }

    defaultConfig {
        applicationId = "com.app.divine"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    api(libs.sdp.android)
    api(libs.ssp.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.annotation)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":core"))
    implementation(project(":firebaseSDK"))
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.analytics)

    implementation(libs.androidx.lifecycle.viewmodel.ktx.v250)
    implementation(libs.dagger)
    implementation(libs.dagger.compiler)
    implementation(libs.dagger.android)
    implementation(libs.converter.gson)
    implementation(libs.adapter.rxjava2)
    implementation(libs.stetho.okhttp3)
    implementation(libs.volley)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.support.annotations)
    kapt(libs.androidx.room.compiler)
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.9.0")
    implementation(libs.androidx.swiperefreshlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(libs.glide)

    implementation("io.socket:socket.io-client:2.1.1")
}
