package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.rogallab.mobile.R
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.ui.errors.ErrorParams
import de.rogallab.mobile.ui.errors.ErrorState
import de.rogallab.mobile.ui.errors.showError
import de.rogallab.mobile.ui.people.PersonIntent
import de.rogallab.mobile.ui.people.PersonUiState
import de.rogallab.mobile.ui.people.PersonValidator
import de.rogallab.mobile.ui.people.PersonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
   viewModel: PersonViewModel = viewModel(),
   isInputMode: Boolean = true,
   id: String? = null
) {
   val tag =
      if (isInputMode) "<-PersonInputScreen"
      else "<-PersonDetailScreen"

   // Observe the state of the viewmodel
   val personUiState: PersonUiState
      by viewModel.personUiStateFlow.collectAsStateWithLifecycle()

   // Accessor for the PersonValidator
   val validator: PersonValidator = viewModel.validator

   // is PersonDetailScreen
   if (!isInputMode) {
      id?.let { it: String ->
         LaunchedEffect(Unit) {
            viewModel.onProcessIntent(PersonIntent.FetchById(it))
         }
      } ?: run {
         viewModel.onErrorEvent( ErrorParams(message = "No id for person is given"))
      }
   }

   val snackbarHostState = remember { SnackbarHostState() }
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         TopAppBar(
            title = { Text(text = if (isInputMode) stringResource(R.string.personInput)
                                  else stringResource(R.string.personDetail)) },
            navigationIcon = {
               IconButton(onClick = {
                  logDebug(tag, "Up (reverse) -> PeopleListScreen")


               }) {
                  Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                     contentDescription = stringResource(R.string.back))
               }
            }
         )
      },
      snackbarHost = {
         SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(
               snackbarData = data,
               actionOnNewLine = true
            )
         }
      }
   ){ innerPadding ->
      Column(
         modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .imePadding() // padding for the bottom for the IME
      ) {

         InputName(
            name = personUiState.person.firstName,          // State ↓
            onNameChange = { firstName: String ->           // Event ↑
               viewModel.onProcessIntent(PersonIntent.FirstNameChange(firstName))
            },
            label = stringResource(R.string.firstName),            // State ↓
            validateName = validator::validateFirstName,    // parameter
         )
         InputName(
            name = personUiState.person.lastName,           // State ↓
            onNameChange = { lastName: String ->            // Event ↑
               viewModel.onProcessIntent(PersonIntent.LastNameChange(lastName))
            },
            label = stringResource(R.string.lastName),             // State ↓
            validateName = validator::validateLastName,     // parameter
         )
         InputEmail(
            email = personUiState.person.email ?: "",       // State ↓
            onEmailChange = { email: String ->              // Event ↑
               viewModel.onProcessIntent(PersonIntent.EmailChange(email))
            },
            validateEmail = validator::validateEmail        // parameter
         )
         InputPhone(
            phone = personUiState.person.phone ?: "",       // State ↓
            onPhoneChange = { phone: String ->              // Event ↑
               viewModel.onProcessIntent(PersonIntent.PhoneChange(phone))
            },
            validatePhone = validator::validatePhone        // parameter
         )
      } // Column
   } // Scaffold

   val errorState: ErrorState
      by viewModel.errorStateFlow.collectAsStateWithLifecycle()
   LaunchedEffect(errorState.params) {
      errorState.params?.let { params: ErrorParams ->
         // show the error with a snackbar
         showError(snackbarHostState, params,
            viewModel::onErrorEventHandled)
      }
   }
}