package de.rogallab.mobile

import android.app.Application
import de.rogallab.mobile.domain.utilities.logInfo
import de.rogallab.mobile.ui.errors.ErrorMessages
import de.rogallab.mobile.ui.people.PersonInputValidator

class AppApplication : Application() {

   override fun onCreate() {
      super.onCreate()

      logInfo(TAG, "onCreate()")

      // Singletons are initialized here
      errorMessages = ErrorMessages.apply {
         initialize(applicationContext)
      }
      personValidator = PersonInputValidator.apply {
         initialize(applicationContext)
      }

   }

   companion object {
      const val ISINFO = true
      const val ISDEBUG = true
      const val ISVERBOSE = true
      private const val TAG = "<-AppApplication"

      lateinit var errorMessages: ErrorMessages
         private set
      lateinit var personValidator: PersonInputValidator
         private set

   }

}
