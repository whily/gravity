plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "net.whily.android.gravity.shared"
        compileSdk = 34
        minSdk = 26
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.kotsci)
            implementation(libs.kotland)
        }
    }
}
