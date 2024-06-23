plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "lnbti.gtp01.droidai"
    compileSdk = 34

    defaultConfig {
        applicationId = "lnbti.gtp01.droidai"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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

val navigationVersion = "2.7.7"
val hiltVersion = "2.48.1"
val lifeCycleVersion = "2.7.0"
val retrofitVersion = "2.9.0"
val playServicesLocationVersion = "21.2.0"
val cameraxVersion = "1.3.2"
val glideVersion = "4.16.0"
val materialVersion = "1.11.0"
val mockitoVersion = "5.10.0"
val fragmentTestVersion = "1.6.2"
val coreTestVersion = "2.2.0"
val testRulesVersion = "1.5.0"
val testHamcrestVersion = "2.2"
val junitVersion = "4.13.2"
val extJunitVersion = "1.1.5"
val espressoVersion = "3.5.1"
val testRunnerVersion = "1.5.2"
val coroutineVersion = "1.7.3"
val firestore = "24.6.1"
val firebaseMessaging = "23.4.1"

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.gms:play-services-location:$playServicesLocationVersion")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-firestore:24.11.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ——— Android Navigation component ——— //
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    //  ——— Dagger - Hilt ——— //
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // ——— lifecycle & ViewModel ——— //
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleVersion")

    //  ———  retrofit ——— //
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    //  ———  Camerax ——— //
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-video:${cameraxVersion}")
    implementation("androidx.camera:camera-view:${cameraxVersion}")
    implementation("androidx.camera:camera-extensions:${cameraxVersion}")

    //  ———  Glide ——— //
    implementation("com.github.bumptech.glide:glide:${glideVersion}")
    annotationProcessor("com.github.bumptech.glide:compiler:${glideVersion}")

    //-- Material Version --//
    implementation("com.google.android.material:material:$materialVersion")

    // ——— ActivityTestRule ——— //
    androidTestImplementation("androidx.test:rules:$testRulesVersion")
    androidTestImplementation("androidx.test:runner:$testRunnerVersion")

    // ——— Mockito dependencies ——— //
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    androidTestImplementation("org.mockito:mockito-android:$mockitoVersion")

    //  ——— Testing Library ——— //
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$extJunitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
    androidTestImplementation("org.hamcrest:hamcrest:$testHamcrestVersion")
    implementation("androidx.arch.core:core-testing:$coreTestVersion")
    androidTestImplementation("androidx.fragment:fragment-testing:$fragmentTestVersion")

    // ——— Coroutine Test ——— //
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")

    // ——— Fcm ——— //
    implementation("com.google.firebase:firebase-messaging:$firebaseMessaging")

    //-- PDF reader --//
    implementation("com.github.afreakyelf:Pdf-Viewer:v2.0.5")
}