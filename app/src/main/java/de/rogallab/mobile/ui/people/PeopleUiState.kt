package de.rogallab.mobile.ui.people

import androidx.compose.runtime.Immutable
import de.rogallab.mobile.domain.entities.Person

@Immutable
data class PeopleUiState(
   val isLoading: Boolean = false,
   val isSuccessful: Boolean = false,
   val people: List<Person> = emptyList()
)