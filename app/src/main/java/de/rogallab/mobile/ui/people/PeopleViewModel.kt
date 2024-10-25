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
import de.rogallab.mobile.ui.ResourceProvider
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
   // get error resources from the context
   private val _resourceProvider = ResourceProvider(_context)
   private val _errorResources = ErrorResources(_resourceProvider)

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
            val message = "Failed to fetch people ${resultData.throwable.localizedMessage}"
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
      when (val resultData = _repository.findById(id)) {
         is ResultData.Success -> {
            _personUiStateFlow.update { it: PersonUiState ->
               it.copy(person = resultData.data ?: Person())  // new UiState
            }
         }
         is ResultData.Error -> {
            val message = "Failed to fetch person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
         }
      }
   }
   private fun create() {
      logDebug(TAG, "createPerson")
      when (val resultData = _repository.create(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> {
            val message = "Failed to create a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
         }
      }
   }
   private fun update() {
      logDebug(TAG, "updatePerson")
      when (val resultData = _repository.update(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> {
            val message = "Failed to update a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
         }
      }
   }
   private fun remove(person: Person) {
      logDebug(TAG, "removePerson: $person")
      when (val resultData = _repository.remove(person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> {
            val message = "Failed to remove a person ${resultData.throwable.localizedMessage}"
            logError(TAG, message)
         }
      }
   }

   fun clearState() {
      _personUiStateFlow.update { it.copy(person = Person()) }
   }

   // ===============================
   // N O   S T A T E   C H A N G E S
   // ===============================
   // Validation is unrelated to state management and simply returns a result
   // We can call the validation function directly in the Composables
   fun validateFirstName(firstName: String): Pair<Boolean, String> {
      return if (firstName.isEmpty() || firstName.length < _errorResources.charMin)
         Pair(true, _errorResources.firstnameTooShort)
      else if (firstName.length > _errorResources.charMax)
         Pair(true, _errorResources.firstnameTooLong)
      else
         Pair(false, "")
   }
   fun validateLastName(lastName: String): Pair<Boolean, String> =
      if (lastName.isEmpty() || lastName.length < _errorResources.charMin)
         Pair(true, _errorResources.lastnameTooShort)
      else if (lastName.length > _errorResources.charMax)
         Pair(true, _errorResources.lastnameTooLong)
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
      phone?.let { it ->
         when (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
            true -> return Pair(false, "")   // phone ok
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
      // firstName or lastName too short or to long
      if (person.firstName.isEmpty() || person.firstName.length < charMin) {
         logError(TAG, _errorResources.firstnameTooShort)
      } else if (person.firstName.length > charMax) {
         logError(TAG, _errorResources.firstnameTooLong)
      } else if (person.lastName.isEmpty() || person.lastName.length < charMin) {
         logError(TAG, _errorResources.lastnameTooShort)
      } else if (person.lastName.length > charMax) {
         logError(TAG, _errorResources.lastnameTooLong)
      }
      // email not valid
      else if (person.email != null &&
         !Patterns.EMAIL_ADDRESS.matcher(person.email).matches()) {
         logError(TAG, _errorResources.emailInValid)
      }
      // phone not valid
      else if (person.phone != null &&
         !Patterns.PHONE.matcher(person.phone).matches()) {
         logError(TAG, _errorResources.phoneInValid)
      } else {
         // write data to repository
         if (isInput) this.create()
         else this.update()
      }
   }

   companion object {
      private const val TAG = "<-PeopleViewModel"
   }
}