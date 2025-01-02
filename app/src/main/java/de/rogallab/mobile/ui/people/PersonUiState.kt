package de.rogallab.mobile.ui.people

import androidx.compose.runtime.Immutable
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.newUuid

@Immutable
data class PersonUiState(
   val person: Person = Person(id = newUuid())
)