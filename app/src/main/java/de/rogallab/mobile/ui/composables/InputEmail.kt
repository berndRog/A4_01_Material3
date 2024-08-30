package de.rogallab.mobile.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import de.rogallab.mobile.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputEmail(
   email: String?,                                    // State ↓
   onEmailChange: (String) -> Unit,                   // Event ↑
   validateEmail: (String?) -> Pair<Boolean, String>, // Event ↑
   label: String = stringResource(R.string.email),    // State ↓
) {
   var isError by rememberSaveable { mutableStateOf(false) }
   var errorText by rememberSaveable { mutableStateOf("") }

   var isFocus by rememberSaveable { mutableStateOf(false) }
   val focusManager: FocusManager = LocalFocusManager.current
   val keyboardController = LocalSoftwareKeyboardController.current

   // Reusable Validation Functions: Validate the input when it changes
   val validate: (String?) -> Unit = { input ->
      val (e, t) = validateEmail(input)
      isError = e
      errorText = t
   }

   OutlinedTextField(
      modifier = Modifier.fillMaxWidth()
         .onFocusChanged { focusState ->
            if (!focusState.isFocused && isFocus) validate(email)
            isFocus = focusState.isFocused
         },

      value = email ?: "",                   // State ↓
      onValueChange = { onEmailChange(it) }, // Event ↑

      label = { Text(text = label) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = label
         )
      },
      singleLine = true,

      keyboardOptions = KeyboardOptions(
         keyboardType = KeyboardType.Email,
         imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
         onNext = {
            keyboardController?.hide()
            validate(email)
            if (!isError) {
               keyboardController?.hide()
               focusManager.moveFocus(FocusDirection.Down)
            }
         }
      ),

      isError = isError,
      supportingText = {
         if (isError) {
            Text(
               text = errorText,
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isError) {
            Icon(
               Icons.Filled.Error,
               contentDescription = errorText,
               tint = MaterialTheme.colorScheme.error
            )
         }
      },
   )
}