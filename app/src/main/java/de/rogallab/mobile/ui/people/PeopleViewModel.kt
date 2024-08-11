package de.rogallab.mobile.ui.people

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import de.rogallab.mobile.data.PeopleRepository
import de.rogallab.mobile.data.local.DataStore
import de.rogallab.mobile.domain.ResultData
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logError
import de.rogallab.mobile.ui.navigation.NavEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PeopleViewModel(
   application: Application
): AndroidViewModel(application) {

   // we must fix this by using a dependency injection framework
   private val _context = getApplication<Application>().applicationContext
   private val _dataStore = DataStore(_context)
   private val _repository = PeopleRepository(_dataStore)

   init {
      logDebug(TAG, "init")
      _repository.readDataStore()
   }

   // Data Binding PeopleListScreen <=> PersonViewModel
   private val _peopleUiStateFlow: MutableStateFlow<PeopleUiState> = MutableStateFlow(PeopleUiState())
   val peopleUiStateFlow: StateFlow<PeopleUiState> = _peopleUiStateFlow.asStateFlow()

   // read all people from repository
   fun fetchPeople() {
      logDebug(TAG, "fetchPeople")
      when (val resultData = _repository.getAll()) {
         is ResultData.Success -> {
            _peopleUiStateFlow.update { it: PeopleUiState ->
               it.copy(people = resultData.data.toList())
            }
            logDebug(TAG, "fetchPeople() people.size: ${peopleUiStateFlow.value.people.size}")
         }
         is ResultData.Error -> {
            val message = "Failed to fetch people ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
         }
         else -> Unit
      }
   }

   // Data Binding PersonScreen <=> PersonViewModel
   private val _personUiStateFlow: MutableStateFlow<PersonUiState> = MutableStateFlow(PersonUiState())
   val personUiStateFlow: StateFlow<PersonUiState> = _personUiStateFlow.asStateFlow()

   fun onFirstNameChange(firstName: String) {
      if (firstName == _personUiStateFlow.value.person.firstName) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(firstName = firstName))
      }
   }
   fun onLastNameChange(lastName: String) {
      if (lastName == _personUiStateFlow.value.person.lastName) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(lastName = lastName))
      }
   }
   fun onEmailChange(email: String?) {
      if (email == null || email == _personUiStateFlow.value.person.email) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(email = email))
      }
   }
   fun onPhoneChange(phone: String?) {
      if (phone == null || phone == _personUiStateFlow.value.person.phone) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(phone = phone))
      }
   }

   fun createPerson() {
      logDebug(TAG, "createPerson")
      when (val resultData = _repository.create(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetchPeople()
         is ResultData.Error -> {
            val message = "Failed to create a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
            // showOnError(message = message, navEvent = NavEvent.ToPeopleList)
         }
         else -> Unit
      }
   }

   fun removePerson(personId: String) {
      logDebug(TAG, "removePerson: $personId")
      when(val resultData = _repository.remove(personId)) {
         is ResultData.Success -> fetchPeople()
         is ResultData.Error -> {
            val message = "Failed to remove a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
            //showOnError(message = message, navEvent = NavEvent.ToPeopleList)
         }
         else -> Unit
      }
   }

   fun clearState() {
      _personUiStateFlow.update { it.copy(person = Person()) }
   }

   fun validate(
      isInput: Boolean
   ) {
      // input is ok        -> add and navigate up
      // detail is ok       -> update and navigate up
      // is the is an error -> show error and stay on screen
/*
      val charMin = _errorMessages.charMin
      val charMax = _errorMessages.charMax

      val person = _personUiStateFlow.value.person
      // firstName or lastName too short
      if (person.firstName.isEmpty() || person.firstName.length < charMin) {
         showOnError(message = _errorMessages.nameTooShort, navEvent = null)
      }
      else if (person.lastName.isEmpty() || person.lastName.length < charMin) {
         showOnError(message = _errorMessages.nameTooShort, navEvent = null)
      }
      else if (person.firstName.length > charMax) {
         showOnError(message = _errorMessages.nameTooLong, navEvent = null)
      }
      else if (person.lastName.length > charMax) {
         showOnError(message = _errorMessages.nameTooLong, navEvent = null)
      }
      else if (person.email != null &&
         !Patterns.EMAIL_ADDRESS.matcher(person.email).matches()) {
         showOnError(message = _errorMessages.emailInValid, navEvent = null)
      }
      else if (person.phone != null &&
         !Patterns.PHONE.matcher(person.phone).matches()) {
         showOnError(message = _errorMessages.phoneInValid, navEvent = null)
      }
      else {
         if (isInput) this.createPerson()
         else         this.updatePerson()
         onNavigateTo(NavEvent.ToPeopleList)
      }
 */
   }

   // lifecycle ViewModel
   // onCleared() is called when the ViewModel is no longer used and will be destroyed
   override fun onCleared() {
      logDebug(TAG, "onCleared()")
      _repository.writeDataStore()
      super.onCleared()
   }

   companion object {
      private const val TAG = "[PeopleViewModel]"
   }
}