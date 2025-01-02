package de.rogallab.mobile.ui.errors

import androidx.compose.material3.SnackbarDuration

data class ErrorParams(
   // error state
   val throwable: Throwable? = null,
   // info message
   val message: String = "",

   // Snackbar parameters
   val actionLabel: String? = "ok",
   // duration of the snackbars visibility
   val duration: SnackbarDuration = SnackbarDuration.Long,
   // undo action
   val withUndoAction: Boolean = false,
   val onUndoAction: () -> Unit = {}, // default action: do nothing

   // navigation to
   // var navEvent: NavEvent?  = null,
)