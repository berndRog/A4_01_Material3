package de.rogallab.mobile.ui.people.composables

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import de.rogallab.mobile.R
import de.rogallab.mobile.domain.utilities.logDebug
import kotlinx.coroutines.delay

@Composable
fun InputEmail(
   email: String,                                       // State ↓
   onEmailChange: (String) -> Unit,                      // Event ↑
   validateEmail: (String?) -> Pair<Boolean, String>,    // Event ↑
   label: String = stringResource(R.string.email),// State ↓
) {

   var localEmail by rememberSaveable { mutableStateOf(email) }
   var isFocus by rememberSaveable { mutableStateOf(false) }
   var isError by rememberSaveable { mutableStateOf(false) }
   var errorText by rememberSaveable { mutableStateOf("") }
   val focusManager = LocalFocusManager.current

   // Update localEmail when email changes
   LaunchedEffect(email) {
      localEmail = email
   }

   // Debounce mechanism to delay onNameChange call
   LaunchedEffect(localEmail) {
      delay(300) // Adjust delay as needed
      if (!isError && localEmail != email) {
         onEmailChange(localEmail) // Update ViewModel
      }
   }

   // Validate email when focus is lost
   fun validateAndPropagateEmail() {
      val (e, t) = validateEmail(localEmail)
      isError = e
      errorText = t
      logDebug("<-InputEmail", "isError $e errorText $t")
      if (!isError && localEmail != email) {
         onEmailChange(localEmail) // Update ViewModel only if valid
      }
   }

   OutlinedTextField(
      modifier = Modifier.fillMaxWidth()
         .onFocusChanged { focusState ->
            logDebug("<-InputEmail","onFocusChanged !focusState.isFocused ${!focusState.isFocused} isFocus $isFocus")
            if (!focusState.isFocused && isFocus) {
               validateAndPropagateEmail()
            }
            isFocus = focusState.isFocused
         },

      value = localEmail,                    // State ↓
      onValueChange = {                      // Event ↑
         localEmail = it
         // onEmailChange(localEmail)  // see debouncing
         if (isError) {
            isError = false
            errorText = ""
         }
      },

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
            validateAndPropagateEmail()
            if (!isError) {
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