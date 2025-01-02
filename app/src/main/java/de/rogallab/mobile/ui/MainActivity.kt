package de.rogallab.mobile.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import de.rogallab.mobile.AppStart
import de.rogallab.mobile.ui.people.PersonViewModel
import de.rogallab.mobile.ui.people.PersonViewModelFactory
import de.rogallab.mobile.ui.people.composables.PeopleListScreen
import de.rogallab.mobile.ui.people.composables.PersonScreen
import de.rogallab.mobile.ui.theme.AppTheme

class MainActivity : BaseActivity(TAG) {

//   private val viewModel: PersonViewModel by viewModels {
//      PersonViewModelFactory(application)
//   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {

         val viewModel: PersonViewModel by viewModels {
            PersonViewModelFactory(application)
         }

         AppTheme {
            PersonScreen(
               viewModel,
               isInputMode = true
            )
//            PeopleListScreen(
//               viewModel = viewModel
//            )

         }
      }
   }

   companion object {
      const val ISINFO = true
      const val ISDEBUG = true
      const val ISVERBOSE = true
      private const val TAG = "<-MainActivity"

   }
}


private fun isInTest(): Boolean {
   return try {
      Class.forName("androidx.test.espresso.Espresso")
      true
   } catch (e: ClassNotFoundException) {
      false
   }
}

class PeopleViewModelFactory(
   private val application: Application
) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
         @Suppress("UNCHECKED_CAST")
         return PersonViewModel(application) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
   }
}