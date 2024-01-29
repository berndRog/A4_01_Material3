package de.rogallab.mobile.ui.people.composables

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import de.rogallab.mobile.ui.people.base.validateName
import de.rogallab.mobile.ui.people.base.validateNameTooLong
import de.rogallab.mobile.ui.people.base.validateNameTooShort

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputName(
   name: String,                        // State ↓
   onNameChange: (String) -> Unit,      // Event ↑
   label: String = "Name",
   errorTooShort: String = "",
   errorTooLong: String = ""
) {
   val context: Context = LocalContext.current
   val focusManager: FocusManager = LocalFocusManager.current
   val keyboardController = LocalSoftwareKeyboardController.current
   var isError by rememberSaveable { mutableStateOf(false) }
   var isFocus by rememberSaveable { mutableStateOf(false) }
   var errorText by rememberSaveable { mutableStateOf("") }

   OutlinedTextField(
      modifier = Modifier
         .padding(horizontal = 8.dp)
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            if (!focusState.isFocused && isFocus) {
               val (e, t) = validateNameTooShort(context, name, errorTooShort)
               isError = e
               errorText = t
            }
            isFocus = focusState.isFocused
         },
      value = name,                 // State ↓
      onValueChange = {
         onNameChange(it)           // Event ↑
         val (e, t) = validateNameTooLong(context, name, errorTooLong)
         isError = e
         errorText = t
      },
      label = { Text(text = label) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(imageVector = Icons.Outlined.Person,
            contentDescription = label)
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
         onNext = {
            keyboardController?.hide()
            val (e, t) = validateName(context, name, errorTooShort, errorTooLong)
            isError = e
            errorText = t
            if (!isError) {
               keyboardController?.hide()
               focusManager.moveFocus(FocusDirection.Down)
            }
         }
      ),
      isError = isError,
      supportingText = {
         if (isError) Text(
            modifier = Modifier.fillMaxWidth(),
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
      },
   )

   @Composable
   fun supportingText(): Unit? {
      if (!isError) return null
      else {
         return Text(
            modifier = Modifier.fillMaxWidth(),
            text = errorText,
            color = MaterialTheme.colorScheme.error
         )
      }
   }
}