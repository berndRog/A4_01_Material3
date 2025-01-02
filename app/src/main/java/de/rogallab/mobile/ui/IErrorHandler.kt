package de.rogallab.mobile.ui

import de.rogallab.mobile.ui.errors.ErrorParams
import de.rogallab.mobile.ui.errors.ErrorState
import kotlinx.coroutines.flow.StateFlow


interface IErrorHandler {
   // current error/info state
   val errorStateFlow: StateFlow<ErrorState>
   // handle error event only
   fun handleErrorEvent(t: Throwable)
   // process error event
   fun onErrorEvent(params: ErrorParams)
   // process error event handled
   fun onErrorEventHandled()
}