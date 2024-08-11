package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rogallab.mobile.R
import de.rogallab.mobile.ui.people.PeopleViewModel
import de.rogallab.mobile.ui.people.PersonUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
   viewModel: PeopleViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
// isInputScreen: Boolean = true,
   id: String? = null
) {
   // Observe the state of the viewmodel
   val personUiState: PersonUiState
      by viewModel.personUiStateFlow.collectAsStateWithLifecycle()

   val screenTitle = stringResource(R.string.person_input)

   Column(modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.surface)
      .padding(horizontal = 8.dp)
      .imePadding() // padding for the bottom for the IME
   ) {

      TopAppBar(
         title = { Text(text = screenTitle) },
      )

      InputName(
         name = personUiState.person.firstName,           // State ↓
         onNameChange = viewModel::onFirstNameChange,    // Event ↑
         label = stringResource(R.string.firstName)               // State ↓
      )
      InputName(
         name = personUiState.person.lastName,        // State ↓
         onNameChange = viewModel::onLastNameChange,  // Event ↑
         label = stringResource(R.string.lastName),   // State ↓
      )
      InputEmail(
         email = personUiState.person.email,               // State ↓
         onEmailChange = viewModel::onEmailChange    // Event ↑
      )
      InputPhone(
         phone = personUiState.person.phone,               // State ↓
         onPhoneChange = viewModel::onPhoneChange    // Event ↑
      )

      Button(
         onClick = {
            // check input fields and save the person
            // viewModel.createPerson()
         },
         modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
         colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = contentColorFor(MaterialTheme.colorScheme.primary)
         )) {
         Text(
            text = stringResource(R.string.save),
            style = MaterialTheme.typography.bodyLarge
         )
      }
      Button(
         onClick = {
            // check input fields and save the person
            // viewModel.createPerson()
         },
         modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
         colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = contentColorFor(MaterialTheme.colorScheme.secondary)
         )) {
         Text(
            text = stringResource(R.string.save),
            style = MaterialTheme.typography.bodyLarge
         )
      }
      Button(
         onClick = {
            // check input fields and save the person
            // viewModel.createPerson()
         },
         modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
         colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = contentColorFor(MaterialTheme.colorScheme.tertiary)
         )
      ) {
         Text(
            text = stringResource(R.string.save),
            style = MaterialTheme.typography.bodyLarge
         )
      }
   } // Column

//   viewModel.params?.let { params: ErrorParams ->
//      LaunchedEffect(params) {
//         // close the keyboard
//         val ime = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//         ime.hideSoftInputFromWindow(view.windowToken, 0)
//         showErrorWithToast(context, params, viewModel::onNavigateTo)
//         viewModel.onErrorEventHandled()
//      }
//   }
}