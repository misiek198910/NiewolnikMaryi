import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "mivs.niewolnik_maryi"
    compileSdk = 36

    signingConfigs {
        create("release") {
            storeFile = file(localProperties.getProperty("MYAPP_RELEASE_STORE_FILE", "brak-sciezki"))
            storePassword = localProperties.getProperty("MYAPP_RELEASE_STORE_PASSWORD", "")
            keyAlias = localProperties.getProperty("MYAPP_RELEASE_KEY_ALIAS", "")
            keyPassword = localProperties.getProperty("MYAPP_RELEASE_KEY_PASSWORD", "")
        }
    }

    defaultConfig {
        manifestPlaceholders += mapOf()
        applicationId = "mivs.niewolnik_maryi"
        minSdk = 26
        targetSdk = 36
        versionCode = 73
        versionName = "1.4.73"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Placeholdery dla Manifestu
        manifestPlaceholders["adMobAppId"] = ""
    }

    buildTypes {
        getByName("debug") {
            // Testowe ID od Google
            manifestPlaceholders["adMobAppId"] = "ca-app-pub-3940256099942544~3347511713"

            buildConfigField("String", "AD_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "AD_START_UNIT_ID", "\"ca-app-pub-3940256099942544/9257395921\"")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")

            val adAppId = localProperties.getProperty("AD_APP_ID", "")
            val bannerId = localProperties.getProperty("AD_BANNER_ID", "")
            val startId = localProperties.getProperty("AD_START_UNIT_ID", "")

            manifestPlaceholders["adMobAppId"] = adAppId

            buildConfigField("String", "AD_BANNER_ID", "\"$bannerId\"")
            buildConfigField("String", "AD_START_UNIT_ID", "\"$startId\"")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    implementation("com.google.android.play:review:2.0.2")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.android.gms:play-services-ads:24.8.0")
    implementation("com.google.android.ump:user-messaging-platform:4.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}