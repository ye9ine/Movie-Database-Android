plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    kotlin("kapt")
}

apply(plugin = "kotlin-kapt")
apply(plugin = "dagger.hilt.android.plugin")

android {
    namespace = "com.yeyint.movie_collection"
    compileSdk = 34
    viewBinding.isEnabled = true

    defaultConfig {
        applicationId = "com.yeyint.movie_collection"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions += "version"
    productFlavors {
        create("development"){
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-debug"
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_TOKEN", "\"a489082bd475daf345706635253e9f35\"")
        }

        create("uat"){
            dimension = "version"
            versionNameSuffix = "-uat"
            applicationIdSuffix = ".uat"
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_TOKEN", "\"a489082bd475daf345706635253e9f35\"")
        }

        create("production"){
            dimension = "version"
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_TOKEN", "\"a489082bd475daf345706635253e9f35\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.databinding:databinding-runtime:8.2.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //splash api
    implementation("androidx.core:core-splashscreen:1.0.1")


    // For hilt Implementation
    implementation("com.google.dagger:hilt-android:2.48")
    annotationProcessor("com.google.dagger:hilt-android-compiler:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    //LiveData and viewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // To use Kotlin coroutines with lifecycle-aware components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    /*swipe refresh*/
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    /*glide*/
    implementation("com.github.bumptech.glide:glide:4.14.2")
    kapt("com.github.bumptech.glide:compiler:4.14.2")

    /*paging3*/
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")

    //room database
    implementation("androidx.room:room-ktx:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")

    /**Retrofit and okhttp lib**/
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    /**Slider image lib**/
    //enableJetifier true in gradle properties
    implementation("com.nineoldandroids:library:2.4.0")
    implementation("com.daimajia.slider:library:1.1.5@aar")
    implementation("com.squareup.picasso:picasso:2.5.2")

    /*photo view*/
    implementation("com.github.chrisbanes:PhotoView:2.0.0")

    /*youtube player*/
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.0.1")

    /*photo view*/
    implementation("com.github.chrisbanes:PhotoView:2.0.0")
}