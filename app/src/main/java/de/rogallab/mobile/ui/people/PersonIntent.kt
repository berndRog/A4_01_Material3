package de.rogallab.mobile.ui.people

sealed class PersonIntent {
   data class  FirstNameChange(val firstName: String) : PersonIntent()
   data class  LastNameChange(val lastName: String) : PersonIntent()
   data class  EmailChange(val email: String) : PersonIntent()
   data class  PhoneChange(val phone: String) : PersonIntent()

   data class FetchPersonById(val id: String) : PersonIntent()
   data object CreatePerson : PersonIntent()
   data object UpdatePerson : PersonIntent()
   data class  RemovePerson(val id: String) : PersonIntent()
}