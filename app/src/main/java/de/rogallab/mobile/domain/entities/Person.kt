package de.rogallab.mobile.domain.entities
import kotlinx.serialization.Serializable

@Serializable
data class Person(
   val firstName: String = "",
   val lastName: String = "",
   val email: String? = null,
   val phone:String? = null,
   val imagePath: String? = "",
   val id: String
)