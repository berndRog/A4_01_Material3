package de.rogallab.mobile.ui.people.base

import android.content.Context
import de.rogallab.mobile.R

fun isNameTooShort(name: String, charMin: Int): Boolean =
   name.isEmpty() || name.length < charMin

fun isNameTooLong(name: String, charMax: Int): Boolean =
   name.length > charMax

fun validateEmail(email: String?): Boolean =
   email?.let {
      when(android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
         true -> false // email ok
         false -> true // email with an error
      }
   } ?: false

fun validatePhone(phone: String?): Boolean =
   phone?.let {
      when(android.util.Patterns.PHONE.matcher(it).matches()) {
         true -> false   // email ok
         false -> true   // email with an error
      }
   } ?: false

fun validateName(
   context: Context,
   name: String,
   errorTooShort: String,
   errorTooLong: String
): Pair<Boolean, String> {
   val charMin = context.getString(R.string.errorCharMin).toInt()
   val charMax = context.getString(R.string.errorCharMax).toInt()
   if (isNameTooShort(name, charMin)) {
      return Pair(true, errorTooShort)
   } else if (isNameTooLong(name, charMax)) {
      return Pair(true, errorTooLong)
   } else {
      return Pair(false, "")
   }
}

fun validateNameTooShort(
   context: Context,
   name: String,
   errorTooShort: String,
): Pair<Boolean, String> {
   val charMin = context.getString(R.string.errorCharMin).toInt()
   if (isNameTooShort(name, charMin)) {
      return Pair(true, errorTooShort)
   } else {
      return Pair(false, "")
   }
}

fun validateNameTooLong(
   context: Context,
   name: String,
   errorTooLong: String,
): Pair<Boolean, String> {
   val charMax = context.getString(R.string.errorCharMax).toInt()
   if (isNameTooLong(name, charMax)) {
      return Pair(true, errorTooLong)
   } else {
      return Pair(false, "")
   }
}