package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
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
import de.rogallab.mobile.R
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logInfo
import de.rogallab.mobile.domain.utilities.logVerbose
import de.rogallab.mobile.ui.errors.ErrorParams
import de.rogallab.mobile.ui.errors.ErrorState
import de.rogallab.mobile.ui.errors.showError
import de.rogallab.mobile.ui.people.PeopleIntent
import de.rogallab.mobile.ui.people.PersonIntent
import de.rogallab.mobile.ui.people.PersonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleListScreen(
   viewModel: PersonViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
   val tag = "<-PeopleListScreen"

   // observe the peopleUiStateFlow in the ViewModel
   val peopleUiState
      by viewModel.peopleUiStateFlow.collectAsStateWithLifecycle()

   val snackbarHostState = remember { SnackbarHostState() }
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         TopAppBar(
            title = { Text(stringResource(R.string.peopleList)) }
         )
      },
      floatingActionButton = {
         FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiary,
            onClick = { logDebug(tag, "FAB clicked") }
         ) {
            Icon(Icons.Default.Add, "Add a contact")
         }
      },
      snackbarHost = {
         SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(
               snackbarData = data,
               actionOnNewLine = true
            )
         }
      }
   ) { innerPadding ->

      val undoDeletePerson = stringResource(R.string.undoDeletePerson)
      val undoAnswer = stringResource(R.string.undoAnswer)

      LazyColumn(
         modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
      ) {
         items(
            items = peopleUiState.people.sortedBy { it.firstName },
            key = { it: Person -> it.id }
         ) { person ->
            logDebug(tag, "Person: ${person.firstName} ${person.lastName}")
//            PersonListItem(
//               id = person.id,
//               firstName = person.firstName,
//               lastName = person.lastName,
//               email = person.email ?: "",
//               phone = person.phone ?: "",
//               onClicked = {
//                  logInfo(tag, "Person clicked: ${person.lastName}")
//               },
//               onDeleted = {
//                  logInfo(tag, "Person deleted: ${person.lastName}")
//                  viewModel.onProcessIntent(PersonIntent.Remove(person))
//                  viewModel.onErrorEvent(
//                     ErrorParams(
//                        message = undoDeletePerson,
//                        actionLabel = undoAnswer,
//                        duration = SnackbarDuration.Long,
//                        withUndoAction = false,
//                        onUndoAction = {
//                           logInfo(tag, "Person undo deletion")
//                           viewModel.onProcessIntent(PersonIntent.Undo)
//                        }
//                     )
//                  )
//               }
//            ) // PersonListItem

            SwipePersonListItem(
               person = person,                        // item
               //onNavigate = viewModel::onNavigate,     // navigate to DetailScreen
               onProcessIntent = {                     // remove item
                  viewModel.onProcessIntent(PersonIntent.Remove(person)) },
               onErrorEvent = viewModel::onErrorEvent, // undo -> show snackbar
               onUndoAction = {                        // undo -> action
                  viewModel.onProcessIntent(PersonIntent.Undo)
               }
            ) {
               PersonCard(
                  firstName = person.firstName,
                  lastName = person.lastName,
                  email = person.email,
                  phone = person.phone,
                  imagePath = person.imagePath,
               )
            }

         } // items
      } // LazyColumn
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