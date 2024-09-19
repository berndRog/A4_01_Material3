package de.rogallab.mobile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.rogallab.mobile.ui.people.composables.PersonScreen
import de.rogallab.mobile.ui.theme.AppTheme

class MainActivity : BaseActivity(TAG) {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContent {
         AppTheme {
            Surface {
               PersonScreen()
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

@Preview(showBackground = true)
@Composable
fun Preview() {
   AppTheme {
      PersonScreen()
   }
}