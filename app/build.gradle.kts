import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.weathery"
    compileSdk = 34

    val properties = Properties()
    properties.load(rootProject.file("local.properties").inputStream())

    defaultConfig {
        applicationId = "com.example.weathery"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            "\"${properties.getProperty("WEATHER_API_KEY")}\""
        )
        buildConfigField("String", "MAP_API_KEY", "\"${properties.getProperty("MAP_API_KEY")}\"")


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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {


    val androidXTestCoreVersion = ("1.4.0")
    val androidXTestExtKotlinRunnerVersion = ("1.1.3")
    val archTestingVersion = ("2.1.0")
    val coroutinesVersion = ("1.5.0")
    val espressoVersion = ("3.4.0")
    val hamcrestVersion = ("1.3")
    val junitVersion = ("4.13.2")
    val robolectricVersion = ("4.5.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //GSON
    implementation("com.google.code.gson:gson:2.10.1")

    //Coroutines Dependencies
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    //Room
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //ViewModel & livedata
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Location
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    //Swipe decorator
    implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")

    //lottie
    implementation("com.airbnb.android:lottie:6.1.0")

    //splash
    implementation("androidx.core:core-splashscreen:1.0.0")

    //testing dependencies


    // AndroidX and Robolectric
    testImplementation("androidx.test.ext:junit-ktx:$androidXTestExtKotlinRunnerVersion")
    testImplementation("androidx.test:core-ktx:$androidXTestCoreVersion")
    testImplementation("org.robolectric:robolectric:4.8")

    // InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")

    // kotlinx-coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    // hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")

    // Dependencies for local unit tests
    testImplementation("junit:junit:$junitVersion")
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testImplementation("androidx.arch.core:core-testing:$archTestingVersion")
    testImplementation("org.robolectric:robolectric:$robolectricVersion")

    // AndroidX Test - JVM testing
    testImplementation("androidx.test:core-ktx:$androidXTestCoreVersion")
    testImplementation("androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion")

    // AndroidX Test - Instrumented testing
    androidTestImplementation("androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.arch.core:core-testing:$archTestingVersion")

}