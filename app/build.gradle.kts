plugins {
   id("com.android.application")
   id("org.jetbrains.kotlin.android")
}

android {
   namespace = "de.rogallab.mobile"
   compileSdk = 34

   defaultConfig {
      applicationId = "de.rogallab.mobile"
      minSdk = 26
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
         proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      }
   }
   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
   }
   kotlinOptions {
      jvmTarget = "17"
   }
   kotlin {
      jvmToolchain(17)
   }
   lint {
      abortOnError = false
      disable  += "unchecked"
   }
   buildFeatures {
      compose = true
   }
   composeOptions {
      kotlinCompilerExtensionVersion = "1.5.3"
   }
   packaging {
      resources {
         excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
   }
}

dependencies {

   // https://developer.android.com/jetpack/androidx/releases/activity
   val activityCompose = "1.8.0"
   implementation("androidx.activity:activity-compose:$activityCompose")

   // https://developer.android.com/jetpack/androidx/releases/core
   val core = "1.12.0"
   implementation("androidx.core:core-ktx:$core")

   // A BOM is a Maven module that declares a set of libraries with their versions.
   // It will greatly simplify the way you define Compose library versions in your
   // Gradle dependencies block.
   // https://developer.android.com/jetpack/compose/bom/bom-mapping
   val compose = "1.5.4"
   implementation(platform("androidx.compose:compose-bom:2023.10.01"))
   implementation("androidx.compose.ui:ui")
   implementation("androidx.compose.ui:ui-graphics")
   implementation("androidx.compose.ui:ui-tooling-preview")
   val material3 = "1.1.2"
   implementation("androidx.compose.material3:material3:$material3")
   implementation("androidx.compose.material:material-icons-extended:$compose")

   // Lifecycle,
   // https://developer.android.com/jetpack/androidx/releases/lifecycle
   val lifecycle = "2.6.2"
   // val archVersion = "2.2.0"
   // ViewModel
   implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
   // ViewModel utilities for Compose
   implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle")
   // LiveData
   implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")

   // Coroutines
   // https://kotlinlang.org/docs/releases.html
   val kotlinCoroutines = "1.7.3"
   implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutines")
   implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutines")

   // Image loading
   // https://coil-kt.github.io/coil/
   val coilCompose = "2.4.0"
   implementation("io.coil-kt:coil-compose:$coilCompose")

   // TESTS -----------------------
   testImplementation("junit:junit:4.13.2")

   // ANDROID TESTS ---------------
   // https://developer.android.com/jetpack/androidx/releases/test
   val androidTestCore = "1.5.0"
   // To use the androidx.test.core APIs
   androidTestImplementation("androidx.test:core:$androidTestCore")
   androidTestImplementation("androidx.test:core-ktx:$androidTestCore")

   // To use the androidx.test.espresso
   val espresso = "3.5.1"
   androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")

   // To use the JUnit Extension APIs
   val extJunit = "1.1.5"
   androidTestImplementation("androidx.test.ext:junit:$extJunit")
   androidTestImplementation("androidx.test.ext:junit-ktx:$extJunit")

   // To use the Truth Extension APIs
   val truth = "1.5.0"
   androidTestImplementation("androidx.test.ext:truth:$truth")

   // To use the androidx.test.runner APIs
   val runner = "1.5.2"
   androidTestImplementation("androidx.test:runner:$runner")

   // To use Compose Testing
   androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
   androidTestImplementation("androidx.compose.ui:ui-test-junit4")
   debugImplementation("androidx.compose.ui:ui-tooling")
   val uiTestManifest = "1.5.4"
   debugImplementation("androidx.compose.ui:ui-test-manifest:$uiTestManifest")

   androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutines")
}