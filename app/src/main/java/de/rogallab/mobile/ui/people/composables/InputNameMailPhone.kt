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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import de.rogallab.mobile.R
import de.rogallab.mobile.utilities.logDebug

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputNameMailPhone(
   firstName: String,                        // State ↓
   onFirstNameChange: (String) -> Unit,      // Event ↑
   lastName: String,                         // State ↓
   onLastNameChange: (String) -> Unit,       // Event ↑
   email: String?,                           // State ↓
   onEmailChange: (String) -> Unit,          // Event ↑
   phone: String?,                           // State ↓
   onPhoneChange: (String) -> Unit,          // Event ↑
) {
// val tag = "ok>InputNameMailPhone ."
   val context = LocalContext.current
   val focusManager = LocalFocusManager.current

   val textFirstName = stringResource(R.string.firstName)
   val textLastName = stringResource(R.string.lastName)
   val textEmail = stringResource(R.string.email)
   val textPhone = stringResource(R.string.phone)
   val charMin = stringResource(R.string.errorCharMin).toInt()
   val charMax = stringResource(R.string.errorCharMax).toInt()

   var isErrorFirstName by rememberSaveable { mutableStateOf(false) }
   var isFirstNameFocus by rememberSaveable { mutableStateOf(false) }
   var errorTextFirstName by rememberSaveable { mutableStateOf("") }
   OutlinedTextField(
      modifier = Modifier
         .padding(horizontal = 8.dp)
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            if (!focusState.isFocused && isFirstNameFocus) {
               val (e, t) = isNameTooShort(firstName, charMin,
                  getString(context,R.string.errorFirstNameTooShort))
               isErrorFirstName = e
               errorTextFirstName = t
            }
            isFirstNameFocus = focusState.isFocused
         },
      value = firstName,                 // State ↓
      onValueChange = {
         onFirstNameChange(it)           // Event ↑
         val (e, t) = isNameTooLong(it, charMax,
            getString(context,R.string.errorFirstNameTooLong))
         isErrorFirstName = e
         errorTextFirstName = t
      },
      label = { Text(text = textFirstName) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(imageVector = Icons.Outlined.Person,
            contentDescription = textFirstName)
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions.Default.copy(
         imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
         onNext = {
            val (e, t) = isNameTooShort(firstName, charMin,
               getString(context,R.string.errorFirstNameTooShort))
            isErrorFirstName = e
            errorTextFirstName = t
            if(!isErrorFirstName) {
               val (e2, t2) = isNameTooLong(firstName, charMax,
                  getString(context,R.string.errorFirstNameTooLong))
               isErrorFirstName = e2
               errorTextFirstName = t2
            }
            if(!isErrorFirstName) focusManager.moveFocus(FocusDirection.Down)

         }
      ),
      isError = isErrorFirstName,
      supportingText = {
         if (!isErrorFirstName) {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = stringResource(R.string.okChars),
               color = MaterialTheme.colorScheme.onPrimaryContainer
            )
         } else {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = errorTextFirstName,
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isErrorFirstName)
            Icon(
               imageVector = Icons.Filled.Error,
               contentDescription = errorTextFirstName,
               tint = MaterialTheme.colorScheme.error
            )
      },
   )

   var isErrorLastName by rememberSaveable { mutableStateOf(false) }
   var isLastNameFocus by rememberSaveable { mutableStateOf(false) }
   var errorTextLastName by rememberSaveable { mutableStateOf("") }
   OutlinedTextField(
      modifier = Modifier
         .padding(horizontal = 8.dp)
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            if (!focusState.isFocused && isLastNameFocus) {
               val (e, t) = isNameTooShort(lastName, charMin,
                  getString(context,R.string.errorFirstNameTooShort))
               isErrorLastName = e
               errorTextLastName = t
            }
            isLastNameFocus = focusState.isFocused
         },
      value = lastName,                  // State ↓
      onValueChange = {
         onLastNameChange(it)            // Event ↑
         val (e, t) = isNameTooLong(it, charMax,
            getString(context,R.string.errorFirstNameTooLong))
         isErrorLastName = e
         errorTextLastName = t

      },
      label = { Text(text = lastName) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(imageVector = Icons.Outlined.Person,
            contentDescription = lastName )
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
         imeAction = ImeAction.Next
      ),
      keyboardActions = KeyboardActions(
         onNext = {
            val (e, t) = isNameTooShort(lastName, charMin,
               getString(context,R.string.errorLastNameTooShort))
            isErrorLastName = e
            errorTextLastName = t
            if(!isErrorLastName) {
               val (e2, t2) = isNameTooLong(lastName, charMax,
                  getString(context,R.string.errorFirstNameTooLong))
               isErrorLastName = e2
               errorTextLastName = t2
            }
            if(!isErrorLastName) focusManager.moveFocus(FocusDirection.Down)
         }
      ),
      isError = isErrorLastName,
      supportingText = {
         if (!isErrorLastName) {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = stringResource(R.string.okChars),
               color = MaterialTheme.colorScheme.onPrimaryContainer
            )
         } else {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = errorTextLastName,
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isErrorLastName)
            Icon(
               imageVector = Icons.Filled.Error,
               contentDescription = errorTextLastName,
               tint = MaterialTheme.colorScheme.error
            )
      },
   )

   var isErrorEmail by rememberSaveable { mutableStateOf(false) }
   var isEmailFocus by rememberSaveable { mutableStateOf(false) }
   var errorTextEmail by rememberSaveable { mutableStateOf("") }
   OutlinedTextField(
      modifier = Modifier
         .padding(horizontal = 8.dp)
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            if (!focusState.isFocused && isEmailFocus) {
               val (e, t) = validateEmail(email, getString(context,R.string.errorEmail))
               isErrorEmail = e
               errorTextEmail = t
            }
            isEmailFocus = focusState.isFocused
         },
      value = email ?: "",
      onValueChange = { onEmailChange(it) }, // Event ↑
      label = { Text(text = textEmail) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = textEmail
         )},
      singleLine = true,
      keyboardOptions = KeyboardOptions(
         keyboardType = KeyboardType.Email,
         imeAction = ImeAction.Next
      ),
      // check if keyboard action is clicked
      keyboardActions = KeyboardActions(
         onNext = {
            val(e,t) = validateEmail(email, getString(context,R.string.errorEmail))
            isErrorEmail = e
            errorTextEmail = t
            if(!isErrorEmail) focusManager.moveFocus(FocusDirection.Down)
         }
      ),
      isError = isErrorEmail,
      supportingText = {
         if (isErrorEmail) { Text(
            modifier = Modifier.fillMaxWidth(),
            text = errorTextEmail,
            color = MaterialTheme.colorScheme.error
         )}
      },
      trailingIcon = {
         if (isErrorEmail)
            Icon(
               Icons.Filled.Error,
               contentDescription = errorTextEmail,
               tint = MaterialTheme.colorScheme.error
            )
      },
   )

   var isErrorPhone by rememberSaveable { mutableStateOf(false) }
   var isPhoneFocus by rememberSaveable { mutableStateOf(false) }
   var errorTextPhone by rememberSaveable { mutableStateOf("") }
   OutlinedTextField(
      modifier = Modifier
         .padding(horizontal = 8.dp)
         .fillMaxWidth()
         .onFocusChanged { focusState ->
            if (!focusState.isFocused && isPhoneFocus) {
               val (e, t) = validatePhone(phone, getString(context,R.string.errorEmail))
               isErrorPhone = e
               errorTextPhone = t
            }
            isPhoneFocus = focusState.isFocused
         },
      value = phone ?: "",
      onValueChange = { onPhoneChange(it) }, // Event ↑
      label = { Text(text = textPhone) },
      textStyle = MaterialTheme.typography.bodyLarge,
      leadingIcon = {
         Icon(
            imageVector = Icons.Outlined.Phone,
            contentDescription = textPhone)
      },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
         keyboardType = KeyboardType.Phone,
         imeAction = ImeAction.Done
      ),
      // check when keyboard action is clicked
      keyboardActions = KeyboardActions(
         onDone = {
            val(e,t) = validatePhone(phone, getString(context,R.string.errorPhone))
            isErrorPhone = e
            errorTextPhone = t
            if(!isErrorPhone) focusManager.clearFocus() // close keyboard
         }
      ),
      isError = isErrorPhone,
      supportingText = {
         if (isErrorPhone) {
            Text(
               modifier = Modifier.fillMaxWidth(),
               text = errorTextPhone,
               color = MaterialTheme.colorScheme.error
            )
         }
      },
      trailingIcon = {
         if (isErrorPhone)
            Icon(
               imageVector = Icons.Filled.Error,
               contentDescription = errorTextPhone,
               tint = MaterialTheme.colorScheme.error
            )
      },
   )
}

fun isNameTooShort(
   name: String,
   charMin: Int,
   text: String
): Pair<Boolean, String> {
   var localErrorText = ""
   // length < charMin
   val localError = name.isEmpty() || name.length < charMin
   if (localError) {
      localErrorText = text
      logDebug("ok>validateName", localErrorText)
   }
   return Pair(localError, localErrorText)
}

fun isNameTooLong(
   name: String,
   charMax: Int,
   text: String,
): Pair<Boolean, String> {
   var localErrorText = ""
   // length > charMax
   val localError = name.length > charMax
   if (localError) {
      localErrorText = text
      logDebug("ok>validateName", localErrorText)
   }
   return Pair(localError, localErrorText)
}

fun validateEmail(
   email: String?,
   text:String
): Pair<Boolean,String> {
   var localErrorText = ""
   var localError = false
   email?.let {
      localError = ! android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
      if (localError) {
         localErrorText = text
         logDebug("ok>validateEmail", localErrorText)
      }
   }
   return Pair(localError, localErrorText)
}

fun validatePhone(
   phone: String?,
   text:String
): Pair<Boolean,String> {
   var localErrorText = ""
   var localError = false
   phone?.let {
      localError = ! android.util.Patterns.PHONE.matcher(it).matches()
      if (localError) {
         localErrorText = text
         logDebug("ok>validatePhone", localErrorText)
      }
   }
   return Pair(localError, localErrorText)
}