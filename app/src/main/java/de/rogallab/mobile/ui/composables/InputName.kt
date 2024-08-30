package de.rogallab.mobile.ui.composables

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputName(
   name: String,                                   // State ↓
   onNameChange: (String) -> Unit,                 // Event ↑
   label: String = "Name",                         // State ↓
   validateName: (String) -> Pair<Boolean, String> // Event ↑
) {
   // local error state
   var isError by rememberSaveable { mutableStateOf(false) }
   var errorText by rememberSaveable { mutableStateOf("") }
   // debounce job
   var debounceJob: Job? by remember { mutableStateOf(null) }
   val coroutineScope = rememberCoroutineScope()

   // Reusable Validation Functions: Validate the input when it changes
   val validate: (String) -> Unit = { input ->
      val (e, t) = validateName(input)
      isError = e
      errorText = t
   }

   var isFocus by rememberSaveable { mutableStateOf(false) }
   val focusManager: FocusManager = LocalFocusManager.current
   val keyboardController = LocalSoftwareKeyboardController.current

   OutlinedTextField(
      modifier = Modifier
         .testTag("InputNameField") // Set the test tag here
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            if (!focusState.isFocused && isFocus) validate(name)
            isFocus = focusState.isFocused
         },
      value = name,                 // State ↓
      onValueChange = {
         onNameChange(it)           // Event ↑
         // Delay validation until the user stops typing to avoid excessive recompositions.
         debounceJob?.cancel()
         debounceJob = coroutineScope.launch {
            delay(300)
            validate(it)
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
            validate(name)
            if (!isError) {
               keyboardController?.hide()
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