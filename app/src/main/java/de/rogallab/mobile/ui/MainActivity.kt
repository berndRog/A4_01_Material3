package de.rogallab.mobile.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import de.rogallab.mobile.AppApplication
import de.rogallab.mobile.ui.people.PeopleViewModel
import de.rogallab.mobile.ui.people.composables.PersonScreen
import de.rogallab.mobile.ui.theme.AppTheme

class MainActivity : BaseActivity(TAG) {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {

         val viewModel : PeopleViewModel = viewModel()

         AppTheme {
            Surface {
               PersonScreen(
                  viewModel,
                  validator = AppApplication.personValidator,
                  isInputMode = true
               )
               // PeopleListScreen()
               //PeopleSwipeListScreen()
            }
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
      if (modelClass.isAssignableFrom(PeopleViewModel::class.java)) {
         @Suppress("UNCHECKED_CAST")
         return PeopleViewModel(application) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
   }
}