package de.rogallab.mobile.domain.utilities

import android.util.Log
import de.rogallab.mobile.MainActivity.Companion.ISDEBUG
import de.rogallab.mobile.MainActivity.Companion.ISINFO
import de.rogallab.mobile.MainActivity.Companion.ISVERBOSE

fun logError(tag: String, message: String) {
   val msg = formatMessage(message)
   Log.e(tag, msg)
}
fun logWarning(tag: String, message: String) {
   val msg = formatMessage(message)
   Log.w(tag, msg)
}
fun logInfo(tag: String, message: String) {
   val msg = formatMessage(message)
   if(ISINFO) Log.i(tag, msg)
}

fun logDebug(tag: String, message: String) {
   val msg = formatMessage(message)
   if (ISDEBUG) Log.d(tag, msg)
}

fun logVerbose(tag: String, message: String) {
   if (ISVERBOSE) Log.v(tag, message)
}

private fun formatMessage(message: String) =
   String.format("%-70s %s", message, Thread.currentThread().toString())