package de.rogallab.mobile.model

import de.rogallab.mobile.utilities.as8
import java.util.*

data class Person(
   val firstName: String,
   val lastName: String,
   val email: String? = null,
   val phone:String? = null,
   val imagePath: String? = "",
   val id: UUID = UUID.randomUUID()
){
   fun asString() : String = "$firstName $lastName} ${id.as8()}"
}
