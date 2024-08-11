package de.rogallab.mobile.ui.navigation

data class NavState(
   var event: NavEvent? = null,
   var onNavRequestHandled: () -> Unit = {}
)
