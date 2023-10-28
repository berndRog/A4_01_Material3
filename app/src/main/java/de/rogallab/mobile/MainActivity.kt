package de.rogallab.mobile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import de.rogallab.mobile.ui.people.PeopleViewModel
import de.rogallab.mobile.ui.people.PersonInputScreen
import de.rogallab.mobile.ui.theme.AppTheme

class MainActivity : BaseActivity(tag) {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      setContent {

         // 2) create a viewModel in an composable lambda expression = function
         val viewModel: PeopleViewModel = viewModel<PeopleViewModel>()
//       val viewModel2 = viewModel<PeopleViewModel>()
//       val viewModel3: PeopleViewModel = viewModel()

         AppTheme {
            // A surface container using the 'background' color from the theme
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
               PersonInputScreen(viewModel)
//             PersonInputScreen()
            }
         }
      }
   }

   companion object {
      const val isInfo = true
      const val isDebug = true
      //12345678901234567890123
      private const val tag = "ok>MainActivity       ."

   }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
   AppTheme {
      PersonInputScreen()
   }
}