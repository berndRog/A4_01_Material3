package de.rogallab.mobile.model

import java.util.*

data class Person(
   val firstName: String,
   val lastName: String,
   val email: String? = null,
   val phone:String? = null,
   val imagePath: String? = "",
   val id: UUID = UUID.randomUUID()
)
