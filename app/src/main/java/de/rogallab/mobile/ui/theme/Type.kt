package de.rogallab.mobile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import de.rogallab.mobile.R

val provider = GoogleFont.Provider(
   providerAuthority = "com.google.android.gms.fonts",
   providerPackage = "com.google.android.gms",
   certificates = R.array.com_google_android_gms_fonts_certs
)

// GoogleFont Roboto
val bodyFontFamily = FontFamily(
   Font(
      googleFont = GoogleFont("Roboto"),
      fontProvider = provider,
   )
)

val displayFontFamily = FontFamily(
   Font(
      googleFont = GoogleFont("Roboto"),
      fontProvider = provider,
   )
)

// GoogleFont IBM Plex Serif
//val bodyFontFamily = FontFamily(
//   Font(
//      googleFont = GoogleFont("IBM Plex Serif"),
//      fontProvider = provider,
//   )
//)
//
//val displayFontFamily = FontFamily(
//   Font(
//      googleFont = GoogleFont("IBM Plex Serif"),
//      fontProvider = provider,
//   )
//)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
   displayLarge = baseline.displayLarge.copy(
      fontFamily = displayFontFamily,
   ),
   displayMedium = baseline.displayMedium.copy(
      fontFamily = displayFontFamily
   ),
   displaySmall = baseline.displaySmall.copy(
      fontFamily = displayFontFamily
   ),

   headlineLarge = baseline.headlineLarge.copy(
      fontFamily = displayFontFamily,
      fontSize   = 36.sp, // 32.sp,
      lineHeight = 40.sp  // 40.sp,
   ),
   headlineMedium = baseline.headlineMedium.copy(
      fontFamily = displayFontFamily,
      fontSize   = 30.sp, // 28.sp,
      lineHeight = 36.sp  // 36.sp,
   ),
   headlineSmall = baseline.headlineSmall.copy(
      fontFamily = displayFontFamily,
      fontSize   = 24.sp, // 24.sp,
      lineHeight = 32.sp  // 32.sp
   ),

   titleLarge = baseline.titleLarge.copy(
      fontFamily = displayFontFamily,
      fontSize   = 26.sp, // 22.sp,
      lineHeight = 28.sp  // 28.sp,
   ),
   titleMedium = baseline.titleMedium.copy(
      fontFamily = displayFontFamily,
      fontSize   = 20.sp, // 16.sp,
      lineHeight = 24.sp  // 24.sp,
   ),
   titleSmall = baseline.titleSmall.copy(
      fontFamily = displayFontFamily,
      fontSize   = 18.sp, // 14.sp,
      lineHeight = 20.sp  // 20.sp,
   ),

   bodyLarge = baseline.bodyLarge.copy(
      fontFamily = bodyFontFamily,
      fontSize   = 18.sp, // 16.sp,
      lineHeight = 24.sp  // 24.sp,
   ),
   bodyMedium = baseline.bodyMedium.copy(
      fontFamily = bodyFontFamily,
      fontSize   = 16.sp, // 14.sp,
      lineHeight = 20.sp  // 20.sp,
   ),
   bodySmall = baseline.bodySmall.copy(
      fontFamily = bodyFontFamily,
      fontSize   = 14.sp, // 12.sp,
      lineHeight = 16.sp  // 16.sp,
   ),

   labelLarge = baseline.labelLarge.copy(
      fontFamily = bodyFontFamily,
      fontSize   = 16.sp, // 14.sp,
      lineHeight = 22.sp  // 20.sp,
   ),
   labelMedium = baseline.labelMedium.copy(
      fontFamily = bodyFontFamily,
      fontSize   = 14.sp, // 12.sp,
      lineHeight = 16.sp  // 16.sp,
   ),
   labelSmall = baseline.labelSmall.copy(
      fontFamily = bodyFontFamily,
      fontSize   = 14.sp, // 11.sp,
      lineHeight = 16.sp  // 16.sp,
   ),
)




/*

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(

   headlineLarge = TextStyle(
      fontSize   = 36.sp, // 32.sp,
      lineHeight = 40.sp  // 40.sp,
   ),
   headlineMedium = TextStyle(
      fontSize   = 30.sp, // 28.sp,
      lineHeight = 36.sp  // 36.sp,
   ),
   headlineSmall = TextStyle(
      fontSize   = 26.sp, // 24.sp,
      lineHeight = 32.sp  // 32.sp,
   ),


   titleLarge = TextStyle(
      fontSize   = 24.sp, // 22.sp,
      lineHeight = 28.sp  // 28.sp,
   ),
   titleMedium = TextStyle(
      fontSize   = 18.sp, // 16.sp,
      lineHeight = 24.sp  // 24.sp,
   ),
   titleSmall = TextStyle(
      fontSize   = 16.sp, // 14.sp,
      lineHeight = 20.sp  // 20.sp,
   ),

   labelLarge = TextStyle(
      fontSize   = 18.sp, // 16.sp,
      lineHeight = 24.sp  // 24.sp,
   ),
   labelMedium = TextStyle(
      fontSize   = 16.sp, // 14.sp,
      lineHeight = 20.sp  // 20.sp,
   ),
   labelSmall = TextStyle(
      fontSize   = 14.sp, // 12.sp,
      lineHeight = 16.sp  // 16.sp,
   ),

   bodyLarge = TextStyle(
      fontSize   = 18.sp, // 16.sp,
      lineHeight = 24.sp  // 24.sp,
   ),
   bodyMedium = TextStyle(
      fontSize   = 16.sp, // 14.sp,
      lineHeight = 20.sp  // 20.sp,
   ),
   bodySmall = TextStyle(
      fontSize   = 14.sp, // 12.sp,
      lineHeight = 16.sp  // 16.sp,
   )
)
*/