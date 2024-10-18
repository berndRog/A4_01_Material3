package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.rogallab.mobile.R
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logError
import de.rogallab.mobile.ui.composables.InputEmail
import de.rogallab.mobile.ui.composables.InputName
import de.rogallab.mobile.ui.composables.InputPhone
import de.rogallab.mobile.ui.people.PeopleViewModel
import de.rogallab.mobile.ui.people.PersonIntent
import de.rogallab.mobile.ui.people.PersonUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
   viewModel: PeopleViewModel = viewModel(),
   isInputMode: Boolean = true,
   id: String? = null
) {
   // Observe the state of the viewmodel
   val personUiState: PersonUiState
      by viewModel.personUiStateFlow.collectAsStateWithLifecycle()

   val screenTitle =
      if (isInputMode) stringResource(R.string.person_input)
      else stringResource(R.string.person_detail)
   val tag =
      if (isInputMode) "<-PersonInputScreen"
      else "<-PersonDetailScreen"

   // is PersonDetailScreen
   if (!isInputMode) {
      id?.let { it: String ->
         LaunchedEffect(Unit) {
            viewModel.onProcessIntent(PersonIntent.FetchById(it))
         }
      } ?: run {
         logError(tag,"No id for person is given")
      }
   }

   val windowInsets = WindowInsets.systemBars
      .add(WindowInsets.ime)
      .add(WindowInsets.safeGestures)

   Column(modifier = Modifier
      .fillMaxSize()
      .verticalScroll(state = rememberScrollState())
      .padding(windowInsets.asPaddingValues())
      .padding(horizontal = 16.dp)
      .imePadding() //
   ) {
      TopAppBar(
         title = { Text(text = screenTitle) },
         navigationIcon = {
            IconButton(onClick = {
               logDebug(tag, "Up (reverse) -> PeopleListScreen")
               // Check input fields and navigate to owners list or show error
               viewModel.validate(isInputMode)
            }) {
               Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = stringResource(R.string.back))
            }
         }
      )

      InputName(
         name = personUiState.person.firstName,          // State ↓
         onNameChange = { firstName: String ->           // Event ↑
            viewModel.onProcessIntent(PersonIntent.FirstNameChange(firstName)) },
         label = stringResource(R.string.firstname),            // State ↓
         validateName = viewModel::validateFirstName,    // Event ↑ no state change
      )
      InputName(
         name = personUiState.person.lastName,           // State ↓
         onNameChange = { lastName: String ->            // Event ↑
            viewModel.onProcessIntent(PersonIntent.LastNameChange(lastName)) },
         label = stringResource(R.string.lastname),             // State ↓
         validateName = viewModel::validateLastName,     // Event ↑ no state change
      )
      InputEmail(
         email = personUiState.person.email,             // State ↓
         onEmailChange = { email:String ->               // Event ↑
            viewModel.onProcessIntent(PersonIntent.EmailChange(email)) },
         validateEmail = viewModel::validateEmail        // Event ↑ no state change
      )
      InputPhone(
         phone = personUiState.person.phone,             // State ↓
         onPhoneChange = { phone:String ->               // Event ↑
            viewModel.onProcessIntent(PersonIntent.PhoneChange(phone)) },
         validatePhone = viewModel::validatePhone        // Event ↑ no state change
      )
   } // Column
}




/*
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
      style = MaterialTheme.typography.titleLarge
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
      style = MaterialTheme.typography.titleLarge
   )
}

*/
