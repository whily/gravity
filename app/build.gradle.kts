import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Signing properties follow the sbt-android convention in local.properties.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

android {
    namespace = "net.whily.android.gravity"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.whily.android.gravity"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"
    }

    signingConfigs {
        val store = localProperties.getProperty("key.store")?.let { rootProject.file(it) }
        if (store != null && store.exists()) {
            create("release") {
                storeFile = store
                storePassword = localProperties.getProperty("key.store.password")
                keyAlias = localProperties.getProperty("key.alias")
                keyPassword = localProperties.getProperty("key.alias.password")
                    ?: localProperties.getProperty("key.store.password")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.kotsci)
    implementation(libs.kotland)
}
