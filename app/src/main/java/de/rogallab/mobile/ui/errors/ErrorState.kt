package de.rogallab.mobile.ui.errors

import androidx.compose.material3.SnackbarDuration
import de.rogallab.mobile.ui.navigation.NavEvent

data class ErrorState(
   // params == null indicates no error
   var params : ErrorParams? = null,
)

data class ErrorParams(
   // Snackbar Parameter
   val message: String = "",
   val actionLabel: String? = null,
   val duration: SnackbarDuration = SnackbarDuration.Short,
   val withDismissAction: Boolean = false,
   val onDismissAction: () -> Unit = {}, // default action: do nothing

   // navigation to route, or if null navigateUp
   var navEvent: NavEvent?  = null,
)