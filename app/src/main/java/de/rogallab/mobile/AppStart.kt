package de.rogallab.mobile

import android.app.Application
import de.rogallab.mobile.domain.utilities.logInfo
import de.rogallab.mobile.ui.people.PersonValidator

class AppStart : Application() {

   override fun onCreate() {
      super.onCreate()

      logInfo(TAG, "onCreate()")

      // Singletons are initialized here
      personValidator = PersonValidator.getInstance(applicationContext)

   }

   companion object {
      const val ISINFO = true
      const val ISDEBUG = true
      const val ISVERBOSE = true
      private const val TAG = "<-AppApplication"

      lateinit var personValidator: PersonValidator
         private set

   }
}