package de.rogallab.mobile.ui.errors

import de.rogallab.mobile.R
import de.rogallab.mobile.ui.ResourceProvider

class ErrorResources(
   resourceProvider: ResourceProvider
) {
   val charMin: Int = resourceProvider.getString(R.string.errorCharMin).toInt()
   val charMax: Int = resourceProvider.getString(R.string.errorCharMax).toInt()
   val firstnameTooShort: String = resourceProvider.getString(R.string.errorFirstnameTooShort)
   val firstnameTooLong: String = resourceProvider.getString(R.string.errorFirstnameTooLong)
   val lastnameTooShort: String = resourceProvider.getString(R.string.errorLastnameTooShort)
   val lastnameTooLong: String = resourceProvider.getString(R.string.errorLastnameTooLong)
   val emailInValid: String = resourceProvider.getString(R.string.errorEmail)
   val phoneInValid: String = resourceProvider.getString(R.string.errorPhone)
}