plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tpf_paii_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tpf_paii_android"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("mysql:mysql-connector-java:5.1.26")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.itextpdf:itext7-core:7.2.2")
    implementation(libs.ui.text.android)
    implementation(libs.swiperefreshlayout)
    implementation(libs.lifecycle.viewmodel.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}