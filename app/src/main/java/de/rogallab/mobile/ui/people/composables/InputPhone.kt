package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import de.rogallab.mobile.R
import de.rogallab.mobile.domain.utilities.logDebug
import kotlinx.coroutines.delay

@Composable
fun InputPhone(
   phone: String,                                    // State ↓
   onPhoneChange: (String) -> Unit,                   // Event ↑
   validatePhone: (String?) -> Pair<Boolean, String>, // Event ↑
   label: String = stringResource(R.string.phone),   // State ↓
) {

   var localPhone by rememberSaveable { mutableStateOf(phone) }
   var isError by rememberSaveable { mutableStateOf(false) }
   var errorText by rememberSaveable { mutableStateOf("") }
   var isFocus by rememberSaveable { mutableStateOf(false) }
   val focusManager = LocalFocusManager.current
// val keyboardController = LocalSoftwareKeyboardController.current

   // Update localName when name changes
   LaunchedEffect(phone) {
      localPhone = phone
   }

   // Debounce mechanism to delay onNameChange call
   LaunchedEffect(localPhone) {
      delay(300) // Adjust delay as needed
      if (!isError && localPhone != phone) {
         onPhoneChange(localPhone)
      }
   }

   // Validate the name when focus is lost
   fun validateAndPropagatePhone() {
      val (e, t) = validatePhone(localPhone)
      isError = e
      errorText = t
      logDebug("<-InputPhone", "isError $e errorText $t")
      if (!isError && localPhone != phone) {
         onPhoneChange(localPhone) // Update ViewModel
      }
   }

   OutlinedTextField(
      modifier = Modifier
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            logDebug("<-InputPhone", "onFocusChanged !focusState.isFocused ${!focusState.isFocused} isFocus $isFocus")
            if (!focusState.isFocused && isFocus) {
               validateAndPropagatePhone()
            }
            isFocus = focusState.isFocused
         },

      value = localPhone,
      onValueChange = {
         localPhone = it
         // onNameChange(localName)  // see debouncing
         if (isError) {
            // Reset error while user is typing
            isError = false
            errorText = ""
         }
      }, // Event ↑
      label = { Text(text = label) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(imageVector = Icons.Outlined.Phone,
            contentDescription = label)
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
         keyboardType = KeyboardType.Phone,
         imeAction = ImeAction.Done
      ),
      // check when keyboard action is clicked
      keyboardActions = KeyboardActions(
         onDone = {
            validateAndPropagatePhone()
            if (!isError) {
//             keyboardController?.hide()
               focusManager.clearFocus()
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
         if (isError)
            Icon(
               imageVector = Icons.Filled.Error,
               contentDescription = errorText,
               tint = MaterialTheme.colorScheme.error
            )
      },
   )
}