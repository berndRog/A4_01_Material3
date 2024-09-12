package de.rogallab.mobile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.rogallab.mobile.ui.people.composables.PeopleSwipeListScreen
import de.rogallab.mobile.ui.people.composables.PersonScreen
import de.rogallab.mobile.ui.theme.AppTheme

class MainActivity : BaseActivity(tag) {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {
         AppTheme {
            Surface {
               PersonScreen()
               //PeopleListScreen()
               //PeopleSwipeListScreen()
            }
         }
      }
   }

   companion object {
      const val isInfo = true
      const val isDebug = true
      const val isVerbose = true
      //12345678901234567890123
      private const val tag = "[MainActivity]"

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

@Preview(showBackground = true)
@Composable
fun Preview() {
   AppTheme {
      PersonScreen()
   }
}