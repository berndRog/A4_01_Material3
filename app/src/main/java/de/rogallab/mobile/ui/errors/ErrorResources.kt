package de.rogallab.mobile.ui.errors

import de.rogallab.mobile.R

class ErrorResources(
   resourceProvider: ResourceProvider
) {
   val charMin: Int = resourceProvider.getString(R.string.errorCharMin).toInt()
   val charMax: Int = resourceProvider.getString(R.string.errorCharMax).toInt()

   val nameTooShort:  String = resourceProvider.getString(R.string.errorNameTooShort)
   val nameTooLong: String = resourceProvider.getString(R.string.errorNameTooLong)
   val emailInValid: String = resourceProvider.getString(R.string.errorEmail)
   val phoneInValid: String = resourceProvider.getString(R.string.errorPhone)
}