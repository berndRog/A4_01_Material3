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
import de.rogallab.mobile.ui.errors.ErrorResources
import de.rogallab.mobile.ui.errors.ResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PeopleViewModel(
   application: Application
): AndroidViewModel(application) {

   // we must fix this by using a dependency injection framework
   private val _context = application.applicationContext
   private val _dataStore = DataStore(_context)
   private val _repository = PeopleRepository(_dataStore)

   // get error resources from the context
   private val _resourceProvider = ResourceProvider(_context)
   private val _errorResources = ErrorResources(_resourceProvider)

   // read dataStore when ViewModel is created
   init {
      logDebug(TAG, "init")
      _repository.readDataStore()
   }
   // write dataStore when ViewModel is cleared
   override fun onCleared() {
      logDebug(TAG, "onCleared()")
      _repository.writeDataStore()
      super.onCleared()
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
         is ResultData.Failure -> {
            val message = "Failed to fetch people ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
         }
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
         is ResultData.Failure -> {
            val message = "Failed to create a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
            // showOnError(message = message, navEvent = NavEvent.ToPeopleList)
         }
      }
   }

   fun updatePerson() {
      logDebug(TAG, "updatePerson")
      when(val resultData = _repository.update(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetchPeople()
         is ResultData.Failure -> {
            val message = "Failed to update a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
         }
      }
   }

   fun removePerson(personId: String) {
      logDebug(TAG, "removePerson: $personId")
      when(val resultData = _repository.remove(personId)) {
         is ResultData.Success -> fetchPeople()
         is ResultData.Failure -> {
            val message = "Failed to remove a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
            //showOnError(message = message, navEvent = NavEvent.ToPeopleList)
         }
      }
   }

   fun clearState() {
      _personUiStateFlow.update { it.copy(person = Person()) }
   }

   fun validateName(name: String): Pair<Boolean, String> =
      if (name.isEmpty() || name.length < _errorResources.charMin)
         Pair(true, _errorResources.nameTooShort)
      else if (name.length > _errorResources.charMax )
         Pair(true, _errorResources.nameTooLong)
      else
         Pair(false, "")

   fun validateEmail(email: String?): Pair<Boolean, String> {
      email?.let {
         when (android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
            true -> return Pair(false, "") // email ok
            false -> return Pair(true, _errorResources.emailInValid)
         }
      } ?: return Pair(false, "")
   }

   fun validatePhone(phone: String?): Pair<Boolean, String> {
      phone?.let {
         when (android.util.Patterns.PHONE.matcher(it).matches()) {
            true -> return Pair(false,"")   // email ok
            false -> return Pair(true, _errorResources.phoneInValid)
         }
      } ?: return Pair(false, "")
   }


   // validate all input fields after user finished input into the form
   fun validate(isInput: Boolean) {
      // input is ok        -> add and navigate up
      // detail is ok       -> update and navigate up
      // is the is an error -> show error and stay on screen
      val charMin = _errorResources.charMin
      val charMax = _errorResources.charMax

      val person = _personUiStateFlow.value.person

      // firstName or lastName too short
      if (person.firstName.isEmpty() || person.firstName.length < charMin) {
         logError(TAG, _errorResources.nameTooShort)
      }
      else if (person.lastName.isEmpty() || person.lastName.length < charMin) {
         logError(TAG, _errorResources.nameTooShort)
      }
      else if (person.firstName.length > charMax) {
         logError(TAG, _errorResources.nameTooLong)
      }
      else if (person.lastName.length > charMax) {
         logError(TAG, _errorResources.nameTooLong)
      }

      // email not valid
      else if (person.email != null &&
         !Patterns.EMAIL_ADDRESS.matcher(person.email).matches()) {
         logError(TAG, _errorResources.emailInValid)
      }

      // phone not valid
      else if (person.phone != null &&
         !Patterns.PHONE.matcher(person.phone).matches()) {
         logError(TAG,_errorResources.phoneInValid)
      }
      else {
         // write data to repository
         if (isInput) this.createPerson()
         else         this.updatePerson()
      }
   }

   companion object {
      private const val TAG = "[PeopleViewModel]"
   }
}