package de.rogallab.mobile.ui.errors

fun isTooShort(value: String, min: Int): Boolean =
   value.isEmpty() || value.length < min

fun isTooLong(value: String, max: Int): Boolean =
   value.length > max

fun validateName(
   name: String,
   min: Int,
   max: Int,
   errorTooShort: String,
   errorTooLong: String
): Pair<Boolean, String> =
   if      (isTooShort(name, min))  Pair(true, errorTooShort)
   else if (isTooLong (name, max))  Pair(true, errorTooLong)
   else                             Pair(false, "")

fun validateNameTooShort(
   name: String,
   min: Int,
   errorTooShort: String,
): Pair<Boolean, String> =
   if (isTooShort(name, min)) Pair(true, errorTooShort)
   else                       Pair(false, "")

fun validateNameTooLong(
   name: String,
   max: Int,
   errorTooLong: String,
): Pair<Boolean, String> =
   if (isTooLong(name, max))  Pair(true, errorTooLong)
   else                       Pair(false, "")


fun validateEmail(email: String?, errorEmail: String): Pair<Boolean, String> {
   email?.let {
      when (android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
         true -> return Pair(false, "") // email ok
         false -> return Pair(true, errorEmail) // email with an error
      }
   } ?: return Pair(false, "")
}

fun validatePhone(phone: String?, errorPhone: String): Pair<Boolean, String> {
   phone?.let {
      when (android.util.Patterns.PHONE.matcher(it).matches()) {
         true -> return Pair(false,"")   // email ok
         false -> return Pair(true,errorPhone)   // email with an error
      }
   } ?: return Pair(false, "")
}
