package de.rogallab.mobile

import android.app.Application
import de.rogallab.mobile.domain.utilities.logInfo

class AppStart : Application() {

   override fun onCreate() {
      super.onCreate()

      logInfo(TAG, "onCreate()")
   }

   companion object {
      const val IS_INFO = true
      const val IS_DEBUG = true
      const val IS_VERBOSE = true
      private const val TAG = "<-AppApplication"
   }
}