package de.rogallab.mobile.utilities

import java.util.UUID

fun UUID.as8(): String =
   this.toString().substring(0..7)+"..."

val UUIDEmpty: UUID
   get() = UUID.fromString("00000000-0000-0000-0000-000000000000")
