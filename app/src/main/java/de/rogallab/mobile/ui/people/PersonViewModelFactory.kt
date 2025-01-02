package de.rogallab.mobile.ui.people

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.rogallab.mobile.ui.IErrorHandler
import de.rogallab.mobile.ui.errors.ErrorHandler

class PersonViewModelFactory(
   private val application: Application,
   private val errorHandler: IErrorHandler = ErrorHandler()
) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
         @Suppress("UNCHECKED_CAST")
         return PersonViewModel(application, errorHandler) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
   }
}