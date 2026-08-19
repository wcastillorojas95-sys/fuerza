plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.lucas.fuerza"
    compileSdk = 35

    defaultConfig {
        // Tercer paquete de la familia, distinto de com.lucas.foco y de
        // com.lucas.habitos: las tres conviven en el mismo telefono.
        applicationId = "com.lucas.fuerza"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    /*
     * Firma fija para las compilaciones de debug.
     *
     * Sin esto, GitHub Actions crea un debug.keystore nuevo en cada compilacion,
     * la firma cambia, y Android se niega a instalar el APK encima del anterior.
     * Con un keystore versionado la firma es siempre la misma.
     *
     * Es el mismo keystore de Foco: no es un secreto, "android" es la contrasena
     * por defecto de Android y solo sirve para depurar.
     */
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("fuerza-debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
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

    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }

    packaging {
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }
}

/*
 * Las mismas dependencias que Foco y ni una mas.
 *
 * No hay Room, ni Retrofit, ni ningun cliente de red: el catalogo va compilado
 * dentro del APK y los entrenos se guardan en un archivo JSON del propio
 * telefono. Menos piezas que se puedan romper y cero dependencia de un servicio
 * ajeno que manana cambie de precio.
 */
dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.material3:material3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}
