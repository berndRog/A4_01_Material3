package de.rogallab.mobile.ui.errors

import android.content.Context
import android.widget.Toast
import de.rogallab.mobile.ui.navigation.NavEvent
import de.rogallab.mobile.ui.people.PeopleViewModel

fun showErrorWithToast(
   context: Context,
   params: ErrorParams,
   onNavigateTo: (NavEvent) -> Unit,
) {
   val message = "!!! ---     " + params.message + "    ---!!!"

   Toast
      .makeText(context,  message, Toast.LENGTH_LONG)
      .show()

   // if navigation is true, navigate to route
   params.navEvent?.let { event ->
      onNavigateTo(event)
   }
}