plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("com.chaquo.python")
}

android {
    namespace = "com.example.my_campus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.my_campus"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        flavorDimensions += "pyVersion"
        productFlavors {
            create("py310") { dimension = "pyVersion" }
            create("py311") { dimension = "pyVersion" }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

chaquopy {
    defaultConfig {
        version = "3.9"
        buildPython("C:\\Program Files\\Python39\\python.exe")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.guava)
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("com.github.yalantis:ucrop:2.2.10") // UCrop latest version
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.play.services.maps)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.google.firebase:firebase-messaging:24.1.1")


    // Testing dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    testImplementation("junit:junit:4.13.2")
}

apply(plugin = "com.google.gms.google-services")
