package de.rogallab.mobile.ui.people

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.rogallab.mobile.AppStart
import de.rogallab.mobile.data.repositories.PersonRepository
import de.rogallab.mobile.data.local.datastore.DataStore
import de.rogallab.mobile.domain.ResultData
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logError
import de.rogallab.mobile.domain.utilities.newUuid
import de.rogallab.mobile.ui.IErrorHandler
import de.rogallab.mobile.ui.errors.ErrorHandler
import de.rogallab.mobile.ui.errors.ErrorParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PersonViewModel(
   application: Application,
   errorHandler: IErrorHandler = ErrorHandler()
) : AndroidViewModel(application),
   IErrorHandler by errorHandler {

   // we must fix this by using a dependency injection framework
   private val _context = application.applicationContext
   private val _dataStore = DataStore(_context)
   private val _repository = PersonRepository(_dataStore)

   // accessed in PersonScreen
   val validator:PersonValidator = PersonValidator(_context)

   //region PeopleListScreen -------------------------------------------------------------------------------
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
         is ResultData.Error -> handleErrorEvent(resultData.throwable)
      }
   }
   //endregion

   //region PersonScreen -------------------------------------------------------------------------------
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
               it.copy(person = resultData.data ?: Person(id = newUuid()))  // new UiState
            }
         }
         is ResultData.Error -> handleErrorEvent(resultData.throwable)
      }
   }

   private fun clear() {
      _personUiStateFlow.update { it.copy(person = Person(id = newUuid() )) }
   }
   private fun create() {
      logDebug(TAG, "createPerson")
      when (val resultData = _repository.create(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> handleErrorEvent(resultData.throwable)
      }
   }
   private fun update() {
      logDebug(TAG, "updatePerson")
      when (val resultData = _repository.update(_personUiStateFlow.value.person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> handleErrorEvent(resultData.throwable)
      }
   }
   private fun remove(person: Person) {
      logDebug(TAG, "removePerson: $person")
      when (val resultData = _repository.remove(person)) {
         is ResultData.Success -> fetch()
         is ResultData.Error -> handleErrorEvent(resultData.throwable)
      }
   }
   //endregion

   //region Validate input fields ------------------------------------------------------------------------
   // validate all input fields after user finished input into the form
   fun validate(isInput: Boolean): Boolean {
      val person = _personUiStateFlow.value.person

      if(validateAndLogError(validator.validateFirstName(person.firstName)) &&
         validateAndLogError(validator.validateLastName(person.lastName)) &&
         validateAndLogError(validator.validateEmail(person.email)) &&
         validateAndLogError(validator.validatePhone(person.phone))
      ) {
         // write data to repository
         if (isInput) this.create()
         else         this.update()
         return true
      } else {
         return false
      }
   }

   private fun validateAndLogError(validationResult: Pair<Boolean, String>): Boolean {
      val (error, message) = validationResult
      if (error) {
         onErrorEvent(ErrorParams(message = message, withUndoAction = false,
            onUndoAction = {} ))  // navEvent = null))
         logError(TAG, message)
         return false
      }
      return true
   }

   companion object {
      private const val TAG = "<-PersonViewModel"
   }
}