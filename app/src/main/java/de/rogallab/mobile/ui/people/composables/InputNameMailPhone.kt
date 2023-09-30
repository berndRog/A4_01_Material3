package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import de.rogallab.mobile.R
import de.rogallab.mobile.utilities.logDebug

@Composable
fun InputNameMailPhone(
   firstName: String,                    // State ↓
   onFirstNameChange: (String) -> Unit,  // Event ↑
   lastName: String,                     // State ↓
   onLastNameChange: (String) -> Unit,   // Event ↑
   email: String?,                       // State ↓
   onEmailChange: (String) -> Unit,      // Event ↑
   phone: String?,                       // State ↓
   onPhoneChange: (String) -> Unit       // Event ↑
) {

   val tag = "ok>InputNameMailPhone ."
   logDebug(tag, "Start")

   val focusManager = LocalFocusManager.current

   val charLimit = 16

   var isErrorFirstName by rememberSaveable { mutableStateOf(false) }
   OutlinedTextField(
      modifier = Modifier.padding(horizontal = 8.dp) .fillMaxWidth(),
      value = firstName,                 // State ↓
      onValueChange = {
         onFirstNameChange(it)           // Event ↑
         isErrorFirstName = it.length > charLimit
      },
      label = { Text(text = stringResource(R.string.firstName)) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(imageVector = Icons.Outlined.Person,
            contentDescription = stringResource(R.string.firstName))
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
         onNext = {
            focusManager.moveFocus(FocusDirection.Down) // next OutlinedTextField
         }
      ),
      isError = isErrorFirstName,
      supportingText = {
         if (!isErrorFirstName) {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = stringResource(R.string.charLimit),
               color = MaterialTheme.colorScheme.onPrimaryContainer
            )
         } else {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = stringResource(R.string.errorFirstName) +
                  ": ${firstName.length}",
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isErrorFirstName)
            Icon(
               imageVector = Icons.Filled.Error,
               contentDescription = stringResource(R.string.errorFirstName) +
                  ": ${firstName.length}",
               tint = MaterialTheme.colorScheme.error
            )
      },
   )


   var isErrorLastName by remember { mutableStateOf(false) }
   OutlinedTextField(
      modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
      value = lastName,                  // State ↓
      onValueChange = {
         onLastNameChange(it)            // Event ↑
         isErrorLastName = it.length > charLimit
      },
      label = { Text(text = stringResource(R.string.lastName)) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(imageVector = Icons.Outlined.Person,
            contentDescription = stringResource(R.string.lastName))
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
         imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
         onNext = {
            focusManager.moveFocus(FocusDirection.Down)
         }
      ),
      isError = isErrorLastName,
      supportingText = {
         if (!isErrorLastName) {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = stringResource(R.string.charLimit),
               color = MaterialTheme.colorScheme.onPrimaryContainer
            )
         } else {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = stringResource(R.string.errorLastName) +
                  ": ${lastName.length}",
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isErrorLastName)
            Icon(
               imageVector = Icons.Filled.Error,
               contentDescription = stringResource(R.string.errorLastName) +
                  ": ${lastName.length}",
               tint = MaterialTheme.colorScheme.error
            )
      },
   )

   var isErrorEmail by rememberSaveable { mutableStateOf(false) }
   OutlinedTextField(
      modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
      value = email ?: "",
      onValueChange = { onEmailChange(it) }, // Event ↑
      label = { Text(text = stringResource(R.string.email)) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = stringResource(R.string.email)
         )},
      singleLine = true,
      keyboardOptions = KeyboardOptions(
         keyboardType = KeyboardType.Email,
         imeAction = ImeAction.Next
      ),
      // check when keyboard action is clicked
      keyboardActions = KeyboardActions(
         onNext = {
            email?.let {
               isErrorEmail = ! isValidEmail(it)
               logDebug(tag, "$it isErrorEmail $isErrorEmail")
            }
            if(!isErrorEmail) focusManager.moveFocus(FocusDirection.Down)
         }
      ),
      isError = isErrorEmail,
      supportingText = {
         if (isErrorEmail) {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = stringResource(R.string.errorEmail),
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isErrorEmail)
            Icon(
               Icons.Filled.Error,
               contentDescription = stringResource(R.string.errorEmail),
               tint = MaterialTheme.colorScheme.error
            )
      },
   )

   var isErrorPhone by rememberSaveable { mutableStateOf(false) }
   OutlinedTextField(
      modifier = Modifier
         .padding(horizontal = 8.dp)
         .fillMaxWidth(),
      value = phone ?: "",
      onValueChange = { onPhoneChange(it) },
      label = { Text(text = stringResource(R.string.phone)) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(
            imageVector = Icons.Outlined.Phone,
            contentDescription = "Phone")
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
         keyboardType = KeyboardType.Phone,
         imeAction = ImeAction.Done
      ),
      // check when keyboard action is clicked
      keyboardActions = KeyboardActions(
         onDone = {
            focusManager.clearFocus() // close keyboard
         }
      ) {
         phone?.let {
            isErrorPhone = ! isValidPhone(it)
         }
      },
      isError = isErrorPhone,
      supportingText = {
         if (isErrorPhone) {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = "Telefonummer ist unzulässig",
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isErrorPhone)
            Icon(
               imageVector = Icons.Filled.Error,
               contentDescription = "Telefonummer ist unzulässig",
               tint = MaterialTheme.colorScheme.error
            )
      },
   )
}

fun isValidEmail(email: String): Boolean {
   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPhone(phone: String): Boolean {
   return android.util.Patterns.PHONE.matcher(phone).matches()
}