package de.rogallab.mobile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import de.rogallab.mobile.ui.features.people.composables.PeopleListScreen
import de.rogallab.mobile.ui.people.composables.PersonScreen
import de.rogallab.mobile.ui.theme.AppTheme

class MainActivity : BaseActivity(tag) {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      // use insets to show to snackbar above ime keyboard for example
      WindowCompat.setDecorFitsSystemWindows(window, false)

      setContent {

         AppTheme {
            Surface(modifier = Modifier
               .fillMaxSize()
               .safeDrawingPadding()
            ) {
  //             PersonScreen()
               PeopleListScreen()
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

@Preview(showBackground = true)
@Composable
fun Preview() {
   AppTheme {
      PersonScreen()
   }
}