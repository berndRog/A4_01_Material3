package de.rogallab.mobile.ui.people

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.rogallab.mobile.R
import de.rogallab.mobile.ui.people.composables.InputMail
import de.rogallab.mobile.ui.people.composables.InputName
import de.rogallab.mobile.ui.people.composables.InputPhone
import de.rogallab.mobile.utilities.logDebug

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonInputScreen(
   // 3) create a viewModel as default value
   viewModel: PeopleViewModel = viewModel()
) {
   val tag = "ok>PersonInputScreen  ."

   Column(
      modifier = Modifier
         .fillMaxWidth()
         .verticalScroll(state = rememberScrollState())
   ) {
      TopAppBar(
         title = { Text(stringResource(R.string.person_input)) },
         navigationIcon = {
            IconButton(onClick = {
               logDebug(tag, "Up.onClick()")
               viewModel.add()
            }) {
               Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = stringResource(R.string.back)
               )
            }
         }
      )
      // InputName(
      //    name = viewModel.firstName,                         // State ↓
      //    onNameChange = { viewModel.onFirstNameChange(it) }, // Event ↑
      //    label = stringResource(R.string.firstName)
      // )
      // instead of using a function inside a lambda
      // a function reference can be used
      InputName(
         name = viewModel.firstName,                  // State ↓
         onNameChange = viewModel::onFirstNameChange, // Event ↑
         label = stringResource(R.string.firstName)   // State ↓
      )
      InputName(
         name = viewModel.lastName,                  // State ↓
         onNameChange = viewModel::onLastNameChange, // Event ↑
         label = stringResource(R.string.lastName)   // State ↓
      )
      InputMail(
         email = viewModel.email,                    // State ↓
         onEmailChange = viewModel::onEmailChange    // Event ↑
      )
      InputPhone(
         phone = viewModel.phone,                    // State ↓
         onPhoneChange = viewModel::onPhoneChange    // Event ↑
      )

      Button(
         modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
         onClick = {
            logDebug(tag, "Button.onClick()")
            viewModel.add()
         }
      ) {
         Text(
            modifier = Modifier.padding(vertical = 4.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(R.string.save)
         )
      }
   }
}