plugins {
   /**
    * Top-level build file where you can add configuration options common to all
    *
    * Use `apply false` in the top-level build.gradle file to add a Gradle
    * plugin as a build dependency but not apply it to the current (root)
    * project. Don't use `apply false` in sub-projects. For more information,
    * see Applying external plugins with same version to subprojects.
    */
   // https://developer.android.com/build/releases/gradle-plugin
   alias(libs.plugins.android.application) apply false
   alias(libs.plugins.kotlin.android) apply false
   alias(libs.plugins.google.devtools.ksp) apply false
   alias(libs.plugins.kotlin.serialization) apply false
   alias(libs.plugins.kotlin.compose.compiler) apply false

}