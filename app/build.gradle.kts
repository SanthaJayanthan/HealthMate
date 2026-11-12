plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // Enable KSP for this module so Room can generate code
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.healthmate"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.healthmate"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Extended icon set (water drop, fire, etc.)
    implementation(libs.androidx.compose.material.icons.extended)
    // Room database (CRUD storage)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    // "ksp" (not "implementation") because the compiler only runs at build time
    ksp(libs.androidx.room.compiler)
    // ViewModel for Compose screens (MVVM)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Navigation between the Dashboard, List and Add/Edit screens
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}