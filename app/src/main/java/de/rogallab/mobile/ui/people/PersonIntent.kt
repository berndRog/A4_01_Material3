package de.rogallab.mobile.ui.people

sealed class PersonIntent {
   data class  FirstNameChange(val firstName: String) : PersonIntent()
   data class  LastNameChange(val lastName: String) : PersonIntent()
   data class  EmailChange(val email: String) : PersonIntent()
   data class  PhoneChange(val phone: String) : PersonIntent()

   data class  FetchById(val id: String) : PersonIntent()
   data object Create : PersonIntent()
   data object Update : PersonIntent()
   data class  Remove(val id: String) : PersonIntent()
}