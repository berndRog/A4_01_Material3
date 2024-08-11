package de.rogallab.mobile.domain.utilities

// UUID is handled as String
fun String.as8(): String = this.substring(0..7)+"..."
val UuidEmpty: String = "00000000-0000-0000-0000-000000000000"
