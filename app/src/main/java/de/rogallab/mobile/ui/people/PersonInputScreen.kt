package de.rogallab.mobile.ui.people

import androidx.compose.foundation.layout.*
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
import de.rogallab.mobile.R
import de.rogallab.mobile.ui.people.composables.InputNameMailPhone
import de.rogallab.mobile.ui.theme.AppTheme
import de.rogallab.mobile.utilities.logDebug

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonInputScreen(
   viewModel: PeopleViewModel =
      androidx.lifecycle.viewmodel.compose.viewModel<PeopleViewModel>()
) {

   val tag = "ok>PersonInputScreen  ."

   Column(
      modifier = Modifier
         .fillMaxWidth()
         .verticalScroll(
            state = rememberScrollState(),
            enabled = true,
            reverseScrolling = true
         )
   ) {

      TopAppBar(
         title = { Text(stringResource(R.string.person_input)) },
         navigationIcon = {
            IconButton(onClick = { }) {
               Icon(
                  imageVector = Icons.Default.ArrowBack,
                  contentDescription = stringResource(R.string.back)
               )
            }
         }
      )

      InputNameMailPhone(
         firstName = viewModel.firstName,                          // State ↓
         onFirstNameChange = { viewModel.onFirstNameChange(it) },  // Event ↑
         lastName = viewModel.lastName,                            // State ↓
         onLastNameChange = { viewModel.onLastNameChange(it) },    // Event ↑
         email = viewModel.email,                                  // State ↓
         onEmailChange = { viewModel.onEmailChange(it) },          // Event ↑
         phone = viewModel.phone,                                  // State ↓
         onPhoneChange = { viewModel.onPhoneChange(it) }           // Event ↑
      )


      Button(
         modifier = Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
         onClick = {
            logDebug(tag, "onClickHandler()")
            val id = viewModel.add()
            // navigate ...
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
