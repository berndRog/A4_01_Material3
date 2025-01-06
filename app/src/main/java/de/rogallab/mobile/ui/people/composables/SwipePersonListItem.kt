package de.rogallab.mobile.ui.people.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.rogallab.mobile.R
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.ui.errors.ErrorParams
import de.rogallab.mobile.ui.people.PersonIntent
import kotlinx.coroutines.delay

@Composable
fun SwipePersonListItem(
   person: Person,
   // onNavigate: (NavEvent) -> Unit,
   onProcessIntent: (PersonIntent) -> Unit,
   onErrorEvent: (ErrorParams) -> Unit,
   onUndoAction: () -> Unit,
   animationDuration: Int = 1000,
   content: @androidx.compose.runtime.Composable () -> Unit
) {

   var isRemoved by remember{ mutableStateOf(false) }
   var isUndo by remember{ mutableStateOf(false) }
   var hasNavigated by remember { mutableStateOf(false) }

   val state: SwipeToDismissBoxState =
      rememberSwipeToDismissBoxState(
         initialValue = SwipeToDismissBoxValue.Settled,
         confirmValueChange = { value: SwipeToDismissBoxValue ->
            if (value == SwipeToDismissBoxValue.StartToEnd && !hasNavigated) {
               //onNavigate(
               //   NavEvent.NavigateForward(NavScreen.PersonDetail.route + "/${person.id}"))
               hasNavigated = true  // call only once
               return@rememberSwipeToDismissBoxState true
            } else if (value == SwipeToDismissBoxValue.EndToStart) {
               isRemoved = true  // with animation
               return@rememberSwipeToDismissBoxState true
            } else return@rememberSwipeToDismissBoxState false
         },
         positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold,
      )

   val undoDeletePerson = stringResource(R.string.undoDeletePerson)
   val undoAnswer = stringResource(R.string.undoAnswer)

   LaunchedEffect(key1 = isRemoved) {
      if(isRemoved) {
         delay(animationDuration.toLong())
         onProcessIntent(PersonIntent.Remove(person))
         // undo remove?
         var params = ErrorParams(
            message = undoDeletePerson,
            actionLabel = undoAnswer,
            duration = SnackbarDuration.Short,
            withUndoAction = false,
            onUndoAction = onUndoAction,
            //navEvent = NavEvent.NavigateReverse(route = NavScreen.PeopleList.route)
         )
         onErrorEvent(params)
      }
   }

   AnimatedVisibility(
      visible = !isRemoved,
      exit = shrinkVertically(
         animationSpec = tween(durationMillis = animationDuration),
         shrinkTowards = Alignment.Top
      ) + fadeOut()
   ) {

      SwipeToDismissBox(
         state = state,
         backgroundContent = { SetSwipeBackground(state) },
         modifier = Modifier.padding(vertical = 4.dp),
         // enable dismiss from start to end (left to right)
         enableDismissFromStartToEnd = true,
         // enable dismiss from end to start (right to left)
         enableDismissFromEndToStart = true
      ) {
         content()
      }
   }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SetSwipeBackground(state: SwipeToDismissBoxState) {

   // Determine the properties of the swipe
   val (colorBox, colorIcon, alignment, icon, description, scale) =
      GetSwipeProperties(state)

   Box(
      Modifier.fillMaxSize()
         .background(
            color = colorBox,
            shape = RoundedCornerShape(10.dp)
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
fun GetSwipeProperties(
   state: SwipeToDismissBoxState
): SwipeProperties {

   // Set the color of the box
   // https://hslpicker.com
   val colorBox: Color = when (state.targetValue) {
      SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surface
      SwipeToDismissBoxValue.StartToEnd -> Color.hsl(120.0f,0.80f,0.30f, 1f) //Color.Green    // move to right
      // move to left  color: dark red
      SwipeToDismissBoxValue.EndToStart -> Color.hsl(0.0f,0.90f,0.40f,1f)//Color.Red      // move to left
   }

   // Set the color of the icon
   val colorIcon: Color = when (state.targetValue) {
      SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.onSurface
      else -> Color.White
   }

   // Set the alignment of the icon
   val alignment: Alignment = when (state.dismissDirection) {
      SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
      SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
      else -> Alignment.Center
   }

   // Set the icon
   val icon: ImageVector = when (state.dismissDirection) {
      SwipeToDismissBoxValue.StartToEnd -> Icons.Outlined.Edit   // left
      SwipeToDismissBoxValue.EndToStart -> Icons.Outlined.Delete // right
      else -> Icons.Outlined.Info
   }

   // Set the description
   val description: String = when (state.dismissDirection) {
      SwipeToDismissBoxValue.StartToEnd -> "Editieren"
      SwipeToDismissBoxValue.EndToStart -> "Löschen"
      else -> "Unknown Action"
   }

   // Set the scale
   val scale = if (state.targetValue == SwipeToDismissBoxValue.Settled)
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