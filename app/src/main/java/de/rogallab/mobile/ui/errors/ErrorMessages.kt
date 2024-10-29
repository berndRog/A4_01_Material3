package de.rogallab.mobile.ui.errors

import android.content.Context
import de.rogallab.mobile.R

object ErrorMessages {

   private lateinit var _context: Context

   fun initialize(context: Context) {
      _context = context.applicationContext
   }

   val charMin: Int by lazy {
      _context.getString(R.string.errorCharMin).toInt()
   }
   val charMax: Int by lazy {
      _context.getString(R.string.errorCharMax).toInt()
   }

   val firstnameTooShort: String by lazy {
      _context.getString(R.string.errorFirstNameTooShort)
   }
   val firstnameTooLong: String by lazy {
      _context.getString(R.string.errorFirstNameTooLong)
   }
   val lastnameTooShort: String by lazy {
      _context.getString(R.string.errorLastNameTooShort)
   }
   val lastnameTooLong: String by lazy {
      _context.getString(R.string.errorLastNameTooLong)
   }
   val emailInValid: String by lazy {
      _context.getString(R.string.errorEmail)
   }
   val phoneInValid: String by lazy {
      _context.getString(R.string.errorPhone)
   }
}