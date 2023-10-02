package de.rogallab.mobile.ui.people

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import de.rogallab.mobile.model.Person
import de.rogallab.mobile.utilities.logDebug

import java.util.*

class PeopleViewModel : ViewModel() {

   var id: UUID = UUID.randomUUID()

   // State = Observables (DataBinding)
   var firstName: String by mutableStateOf(value = "")
      private set
   fun onFirstNameChange(value: String) { firstName = value }

   var lastName: String by mutableStateOf(value = "")
      private set
   fun onLastNameChange(value: String) { lastName = value }

   var email: String? by mutableStateOf(value = null)
      private set
   fun onEmailChange(value: String) { email = value }

   var phone: String? by mutableStateOf(value = null)
      private set
   fun onPhoneChange(value: String) { phone = value }

   var imagePath: String? by mutableStateOf(value = null)
      private set
   fun onImagePathChange(value: String?) { imagePath = value }

   // mutabelList with observer
   val people: SnapshotStateList<Person> = mutableStateListOf()

   init{
//    errorMessage = "Test SnackBar: Fehlermeldung ..."
   }

   // lifecycle ViewModel
   override fun onCleared() {
      logDebug(tag, "onCleared()")
      super.onCleared()
   }

   fun add() {
      val person = getNewPersonFromState()
      logDebug(tag, "add() ${person.firstName} ${person.lastName}")
      people.add(person)
   }

   private fun setStateFromPerson(person: Person) {
      id        = person.id
      firstName = person.firstName
      lastName  = person.lastName
      email     = person.email
      phone     = person.phone
      imagePath = person.imagePath
   }

   private fun getNewPersonFromState(): Person =
      Person(firstName, lastName, email, phone, imagePath)

   companion object {
      private val tag:String = "ok>PeopleViewModel    ."
   }
}