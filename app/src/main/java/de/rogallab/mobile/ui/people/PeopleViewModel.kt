package de.rogallab.mobile.ui.people

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.rogallab.mobile.AppStart
import de.rogallab.mobile.data.repositories.PeopleRepository
import de.rogallab.mobile.data.local.datastore.DataStore
import de.rogallab.mobile.domain.ResultData
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PeopleViewModel(
   application: Application
) : AndroidViewModel(application) {
   // we must fix this by using a dependency injection framework
   private val _context = application.applicationContext
   private val _dataStore = DataStore(_context)
   private val _repository = PeopleRepository(_dataStore)

   private val _validator = AppStart.personValidator

   // ===============================
   // S T A T E   C H A N G E S
   // ===============================
   // PEOPLE LIST SCREEN <=> PeopleViewModel
   private val _peopleUiStateFlow = MutableStateFlow(PeopleUiState())
   val peopleUiStateFlow = _peopleUiStateFlow.asStateFlow()

   // transform intent into an action
   fun onProcessIntent(intent: PeopleIntent) {
      when (intent) {
         is PeopleIntent.Fetch -> fetch()
      }
   }

   // read all people from repository
   private fun fetch() {
      when (val resultData = _repository.getAll()) {
         is ResultData.Success -> {
            _peopleUiStateFlow.update { it: PeopleUiState ->
               it.copy(people = resultData.data.toList())
            }
            logDebug(TAG, "fetch() people.size: ${peopleUiStateFlow.value.people.size}")
         }
         is ResultData.Error -> {
            val message = "Failed to fetch people ${resultData.throwable.message}"
            logError(TAG, message)
         }
      }
   }

   // PERSON SCREEN <=> PeopleViewModel
   private val _personUiStateFlow = MutableStateFlow(PersonUiState())
   val personUiStateFlow = _personUiStateFlow.asStateFlow()

   // transform intent into an action
   fun onProcessIntent(intent: PersonIntent) {
      when (intent) {
         is PersonIntent.FirstNameChange -> onFirstNameChange(intent.firstName)
         is PersonIntent.LastNameChange -> onLastNameChange(intent.lastName)
         is PersonIntent.EmailChange -> onEmailChange(intent.email)
         is PersonIntent.PhoneChange -> onPhoneChange(intent.phone)

         is PersonIntent.ClearState -> clear()
         is PersonIntent.FetchById -> fetchById(intent.id)
         is PersonIntent.Create -> create()
         is PersonIntent.Update -> update()
         is PersonIntent.Remove -> remove(intent.person)
      }
   }

   private fun onFirstNameChange(firstName: String) {
      if (firstName == _personUiStateFlow.value.person.firstName) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(firstName = firstName))
      }
   }
   private fun onLastNameChange(lastName: String) {
      if (lastName == _personUiStateFlow.value.person.lastName) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(lastName = lastName))
      }
   }
   private fun onEmailChange(email: String?) {
      if (email == _personUiStateFlow.value.person.email) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(email = email))
      }
   }
   private fun onPhoneChange(phone: String?) {
      if(phone == _personUiStateFlow.value.person.phone) return
      _personUiStateFlow.update { it: PersonUiState ->
         it.copy(person = it.person.copy(phone = phone))
      }
   }

   private fun fetchById(id: String) {
      logDebug(TAG, "fetchPersonById: $id")
      when (val resultData = _repository.getById(id)) {
         is ResultData.Success -> {
            _personUiStateFlow.update { it: PersonUiState ->
               it.copy(person = resultData.data ?: Person())  // new UiState
            }
         }
         is ResultData.Error -> {
            val message = "Failed to fetch person ${resultData.throwable.message}"
            logError(TAG, message)
         }
      }
   }

   private fun clear() {
      _personUiStateFlow.update { it.copy(person = Person()) }
   }
   private fun create() {
      logDebug(TAG, "createPerson")
      when (val resultData = _repository.create(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> {
            val message = "Failed to create a person ${resultData.throwable.message}"
            logError(TAG, message)
         }
      }
   }
   private fun update() {
      logDebug(TAG, "updatePerson")
      when (val resultData = _repository.update(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> {
            val message = "Failed to update a person ${resultData.throwable.message}"
            logError(TAG, message)
         }
      }
   }
   private fun remove(person: Person) {
      logDebug(TAG, "removePerson: $person")
      when (val resultData = _repository.remove(person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> {
            val message = "Failed to remove a person ${resultData.throwable.message}"
            logError(TAG, message)
         }
      }
   }

   companion object {
      private const val TAG = "<-PeopleViewModel"
   }
}