package de.rogallab.mobile.ui.people.composables

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.rogallab.mobile.R
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logVerbose
import de.rogallab.mobile.ui.composables.SetSwipeBackground
import de.rogallab.mobile.ui.people.PeopleIntent
import de.rogallab.mobile.ui.people.PeopleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleSwipeListScreen(
   viewModel: PeopleViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
   val tag = "<-PeopleListScreen"

   // Observe the peopleUiState of the viewmodel
   val peopleUiState
      by viewModel.peopleUiStateFlow.collectAsStateWithLifecycle()

   // read all people from repository, when the screen is created
   LaunchedEffect(Unit) {
      logVerbose(tag, "readPeople()")
      viewModel.onProcessIntent(PeopleIntent.FetchPeople)
   }

   val activity = LocalContext.current as Activity

   // Back navigation
   BackHandler { activity.finish() }

   val screenTitle = stringResource(R.string.people_list)
   val windowInsets = WindowInsets.systemBars
      .add(WindowInsets.safeGestures)

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(windowInsets.asPaddingValues())
         .padding(horizontal = 8.dp)
   ) {
      TopAppBar( title = { Text(screenTitle) } )

      Row( modifier = Modifier.padding(end = 8.dp)) {
         Spacer(modifier = Modifier.weight(0.8f))
         FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiary,
            onClick = {
               logDebug(tag, "FAB clicked -> Navigate to PersonInputScreen")
            }
         ) {
            Icon(Icons.Default.Add, "Add a contact")
         }
      }

      LazyColumn(
         modifier = Modifier
            .padding(top = 16.dp)
      ) {
         items(
            items = peopleUiState.people.sortedBy { it.firstName },
            key = { it: Person -> it.id }
         ) { person ->

            val swipeToDismissBoxState: SwipeToDismissBoxState =
               rememberSwipeToDismissBoxState(
                  initialValue = SwipeToDismissBoxValue.Settled,
                  confirmValueChange = {
                     if (it == SwipeToDismissBoxValue.StartToEnd) {
                        logDebug(tag, "navigate to PersonDetail")
                        // navigate to PersonScreen
                        // toDo in chapter 5
                        return@rememberSwipeToDismissBoxState true
                     } else if (it == SwipeToDismissBoxValue.EndToStart) {
                        logDebug(tag, "remove Person")
                        // viewModel.removePerson(person)
                        // undo remove?
                        // toDo in chapter 5
                        return@rememberSwipeToDismissBoxState true
                     } else return@rememberSwipeToDismissBoxState false
                  },
                  positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold,
               )

            SwipeToDismissBox(
               state = swipeToDismissBoxState,
               backgroundContent = { SetSwipeBackground(swipeToDismissBoxState) },
               modifier = Modifier.padding(vertical = 4.dp),
               // enable dismiss from start to end (left to right)
               enableDismissFromStartToEnd = true,
               // enable dismiss from end to start (right to left)
               enableDismissFromEndToStart = true
            ) {
               // content
               PersonCard(
                  firstName = person.firstName,
                  lastName = person.lastName,
                  email = person.email,
                  phone = person.phone,
                  imagePath = person.imagePath
               )
            }
         }
      }
   }
}