package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import de.rogallab.mobile.domain.utilities.logDebug
import kotlinx.coroutines.delay

/*
Common input validation patterns in Jetpack Compose include:
1. Immediate Feedback: Validate input as the user types and provide immediate feedback.
2. Debouncing: Delay validation until the user stops typing to avoid excessive recompositions.
3. Single Source of Truth: Maintain input state and validation state in a ViewModel or higher-level composable.
4. Reusable Validation Functions: Create reusable functions for validation logic.
5. Derived State: Use `derivedStateOf` to derive validation state from input state.
6. Visual Cues: Use visual indicators like color changes, icons, and error messages to indicate validation errors.
7. Accessibility: Ensure error messages and input fields are accessible to screen readers.
*/
@Composable
fun InputName(
   name: String,                                   // State ↓
   onNameChange: (String) -> Unit,                 // Event ↑
   label: String = "Name",                         // State ↓
   validateName: (String) -> Pair<Boolean, String>,// Event ↑
) {

   var localName by rememberSaveable { mutableStateOf(name) }
   var isFocus by rememberSaveable { mutableStateOf(false) }
   var isError by rememberSaveable { mutableStateOf(false) }
   var errorText by rememberSaveable { mutableStateOf("") }
   val focusManager = LocalFocusManager.current

   // Update localName when name changes
   LaunchedEffect(name) {
      localName = name
   }

   // Debounce mechanism to delay onNameChange call
   LaunchedEffect(localName) {
      delay(300) // Adjust delay as needed
      if (!isError && localName != name) {
         onNameChange(localName)
      }
   }

   // Validate the name when focus is lost
   fun validateAndPropagateName() {
      val (e, t) = validateName(localName)
      isError = e
      errorText = t
      logDebug("<-InputName", "isError $e errorText $t")
      if (!isError && localName != name) {
         onNameChange(localName) // Update ViewModel
      }
   }

   OutlinedTextField(
      modifier = Modifier
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            logDebug("<-InputName","onFocusChanged !focusState.isFocused ${!focusState.isFocused} isFocus $isFocus")
            if (!focusState.isFocused && isFocus) {
               validateAndPropagateName()
            }
            isFocus = focusState.isFocused
         },

      value = localName,                           // State ↓
      onValueChange = {                            // Event ↑
         localName = it
         // onNameChange(localName)  // see debouncing
         if (isError) {
            // Reset error while user is typing
            isError = false
            errorText = ""
         }
      },

      label = { Text(text = label) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(imageVector = Icons.Outlined.Person, contentDescription = label)
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
         onNext = {
            validateAndPropagateName()
            if (!isError) {
               focusManager.moveFocus(FocusDirection.Down)
            }
         }
      ),
      isError = isError,
      supportingText = {
         if (isError) Text(
            text = errorText,
            color = MaterialTheme.colorScheme.error
         )
      },
      trailingIcon = {
         if (isError) Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = errorText,
            tint = MaterialTheme.colorScheme.error
         )
      }
   )
}

