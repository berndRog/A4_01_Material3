package de.rogallab.mobile.ui.people

import android.content.Context
import android.util.Patterns
import de.rogallab.mobile.ui.errors.ErrorMessages

object PersonInputValidator {

   // get error messages from the string resources
   private lateinit var _errorMessages: ErrorMessages

   fun initialize(context: Context) {
      _errorMessages = ErrorMessages.apply { initialize(context) }
   }

   // Validation is unrelated to state management and simply returns a result
   // We can call the validation function directly in the Composables
   fun validateFirstName(firstName: String): Pair<Boolean, String> =
      if (firstName.isEmpty() || firstName.length < _errorMessages.charMin)
         Pair(true, _errorMessages.firstnameTooShort)
      else if (firstName.length > _errorMessages.charMax )
         Pair(true, _errorMessages.firstnameTooLong)
      else
         Pair(false, "")

   fun validateLastName(lastName: String): Pair<Boolean, String> =
      if (lastName.isEmpty() || lastName.length < _errorMessages.charMin)
         Pair(true, _errorMessages.lastnameTooShort)
      else if (lastName.length > _errorMessages.charMax )
         Pair(true, _errorMessages.lastnameTooLong)
      else
         Pair(false, "")

   fun validateEmail(email: String?): Pair<Boolean, String> {
      email?.let {
         when (android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
            true -> return Pair(false, "") // email ok
            false -> return Pair(true, _errorMessages.emailInValid)
         }
      } ?: return Pair(false, "")
   }

   fun validatePhone(phone: String?): Pair<Boolean, String> {
      phone?.let {
         when (Patterns.PHONE.matcher(it).matches()) {
            true -> return Pair(false,"")   // phone ok
            false -> return Pair(true, _errorMessages.phoneInValid)
         }
      } ?: return Pair(false, "")
   }

}