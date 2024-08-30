package de.rogallab.mobile.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// How to Implement Swipe to Delete with Material3 (veraltet)
// https://www.youtube.com/watch?v=IlI6GgC_j78
// Android Jetpack Compose – Swipe-to-Dismiss mit Material 3
// https://www.geeksforgeeks.org/android-jetpack-compose-swipe-to-dismiss-with-material-3/

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SetSwipeBackground(swipeToDismissBoxState: SwipeToDismissBoxState) {

   // Determine the properties of the swipe
   val (colorBox, colorIcon, alignment, icon, description, scale) =
      determineSwipeProperties(swipeToDismissBoxState)

   Box(
      Modifier
         .fillMaxSize()
         .background(
            color = colorBox,
            shape = RoundedCornerShape(15.dp)
         )
         .padding(horizontal = 16.dp),
      contentAlignment = alignment
   ) {
      Icon(
         icon,
         contentDescription = description,
         modifier = Modifier.scale(scale),
         tint = colorIcon
      )
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun determineSwipeProperties(
   dismissBoxState: SwipeToDismissBoxState
): SwipeProperties {

   // Set the color of the box
   // https://hslpicker.com
   val colorBox: Color = when (dismissBoxState.targetValue) {
      SwipeToDismissBoxValue.Settled -> Color.DarkGray
      SwipeToDismissBoxValue.StartToEnd -> Color.hsl(120.0f,0.80f,0.30f, 1f) //Color.Green    // move to right
      // move to left  color: dark red
      SwipeToDismissBoxValue.EndToStart -> Color.hsl(0.0f,0.90f,0.40f,1f)//Color.Red      // move to left
   }



   // Set the color of the icon
   val colorIcon: Color = when (dismissBoxState.targetValue) {
      SwipeToDismissBoxValue.Settled -> Color.LightGray
      else -> Color.White
   }

   // Set the alignment of the icon
   val alignment: Alignment = when (dismissBoxState.dismissDirection) {
      SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
      SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
      else -> Alignment.Center
   }

   // Set the icon
   val icon: ImageVector = when (dismissBoxState.dismissDirection) {
      SwipeToDismissBoxValue.StartToEnd -> Icons.Outlined.Edit   // left
      SwipeToDismissBoxValue.EndToStart -> Icons.Outlined.Delete // right
      else -> Icons.Outlined.Info
   }



   // Set the description
   val description: String = when (dismissBoxState.dismissDirection) {
      SwipeToDismissBoxValue.StartToEnd -> "Editieren"
      SwipeToDismissBoxValue.EndToStart -> "Löschen"
      else -> "Unknown Action"
   }

   // Set the scale
   val scale = if (dismissBoxState.targetValue == SwipeToDismissBoxValue.Settled)
      1.2f else 1.8f

   return SwipeProperties(
      colorBox, colorIcon, alignment, icon, description, scale)
}

data class SwipeProperties(
   val colorBox: Color,
   val colorIcon: Color,
   val alignment: Alignment,
   val icon: ImageVector,
   val description: String,
   val scale: Float
)