/**
 * Module-level functions
 * These functions are used to provide dependencies for the app.
 *
 * The first section in the build configuration applies the Android Gradle plugin
 * to this build and makes the android block available to specify
 * Android-specific build options.
 */
plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.android)
   alias(libs.plugins.google.devtools.ksp)
   alias(libs.plugins.kotlin.serialization)
   alias(libs.plugins.kotlin.compose.compiler)
}

/**
 * Locate (and possibly download) a JDK used to build your kotlin
 * source code. This also acts as a default for sourceCompatibility,
 * targetCompatibility and jvmTarget. Note that this does not affect which JDK
 * is used to run the Gradle build itself, and does not need to take into
 * account the JDK version required by Gradle plugins (such as the
 * Android Gradle Plugin)
 */
kotlin {
   jvmToolchain(17)
}

android {
   namespace = "de.rogallab.mobile"
   compileSdk = 35

   defaultConfig {
      applicationId = "de.rogallab.mobile"
      minSdk = 32
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

   lint {
      abortOnError = false
      disable  += "unchecked"
   }
   buildFeatures {
      compose = true
      buildConfig = true
   }
   composeOptions {
      kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
   }
   packaging {
      resources {
         excludes += "/META-INF/{AL2.0,LGPL2.1}"
         excludes += "/META-INF/LICENSE.md"
         excludes += "/META-INF/LICENSE-notice.md"
      }
   }
}

dependencies {
   // Gradle version catalo
   // https://www.youtube.com/watch?v=MWw1jcwPK3Q

   // Kotlin
   // https://developer.android.com/jetpack/androidx/releases/core
   implementation(libs.androidx.core.ktx)
   // Kotlin Coroutines
   // https://kotlinlang.org/docs/releases.html
   implementation (libs.kotlinx.coroutines.core)
   implementation (libs.kotlinx.coroutines.android)
   // https://github.com/Kotlin/kotlinx-datetime
   implementation (libs.kotlinx.datetime)
   // Ui Activity
   // https://developer.android.com/jetpack/androidx/releases/activity
   implementation(libs.androidx.activity.compose)
   // Ui Compose
   // https://developer.android.com/jetpack/compose/bom/bom-mapping
   implementation(platform(libs.androidx.compose.bom))
   implementation(libs.androidx.compose.ui)
   implementation(libs.androidx.compose.ui.graphics)
   implementation(libs.androidx.compose.ui.tooling.preview)
   implementation(libs.androidx.compose.material3)
   implementation(libs.androidx.ui.text.google.fonts)
   implementation(libs.material.icons.extended)
   // Ui Camera
   // https://developer.android.com/jetpack/androidx/releases/camera
   implementation(libs.androidx.camera.camera2)
   implementation(libs.androidx.camera.core)
   implementation(libs.androidx.camera.lifecycle)
   implementation(libs.androidx.camera.video)
   implementation(libs.androidx.camera.view)
   implementation(libs.androidx.camera.extensions)

   // Ui Lifecycle
   // https://developer.android.com/jetpack/androidx/releases/lifecycle
   implementation(libs.androidx.lifecycle.viewmodel.ktx)
   // ViewModel utilities for Compose
   implementation(libs.androidx.lifecycle.viewmodel.compose)
   implementation(libs.androidx.lifecycle.runtime.ktx)
   // Lifecycle utilities for Compose
   implementation (libs.androidx.lifecycle.runtime.compose)

   // Ui Navigation
   // https://developer.android.com/jetpack/androidx/releases/navigation
//   implementation(libs.androidx.navigation.ui.ktx)
//   implementation(libs.androidx.navigation.compose)
   // Jetpack Compose Integration
   implementation(libs.androidx.navigation.compose)

   // Image loading
   // https://coil-kt.github.io/coil/
   implementation(libs.coil.compose)

   // Koin
// implementation(libs.koin.androidx.startup)
   implementation(libs.koin.android)
   implementation(libs.koin.androidx.compose)
   implementation (libs.koin.android.compat)

   // JSON Serializer
   implementation(libs.kotlinx.serialization.json)


   // TESTS -----------------------
   testImplementation(libs.junit)
   testImplementation(libs.koin.test)
   // Koin for JUnit 4 / 5
   testImplementation(libs.koin.test.junit4)
   testImplementation(libs.koin.test.junit5)

   // ANDROID TESTS ---------------
   // https://developer.android.com/jetpack/androidx/releases/test
   // To use the androidx.test.core APIs
   androidTestImplementation(libs.androidx.core)
   androidTestImplementation(libs.core.ktx)

   // To use the androidx.test.espresso
   androidTestImplementation(libs.androidx.espresso.core)

   // To use the JUnit Extension APIs
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.junit.ktx)

   // To use the Truth Extension APIs
   androidTestImplementation(libs.androidx.truth)

   // To use the androidx.test.runner APIs
   androidTestImplementation(libs.androidx.runner)

   // To use Compose Testing
   androidTestImplementation(platform(libs.androidx.compose.bom))
   androidTestImplementation(libs.androidx.ui.test.junit4)
   // testing navigation
   androidTestImplementation(libs.androidx.navigation.testing)
   // testing coroutines
   androidTestImplementation(libs.kotlinx.coroutines.test)

   // Koin Test features
   androidTestImplementation(libs.koin.test)
   // Koin for JUnit 4/5
   androidTestImplementation(libs.koin.test.junit4)
   androidTestImplementation(libs.koin.test.junit5)

//   debugImplementation(libs.androidx.ui.tooling)
   debugImplementation(libs.androidx.ui.test.manifest)

}