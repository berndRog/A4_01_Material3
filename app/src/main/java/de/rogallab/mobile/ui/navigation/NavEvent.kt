package de.rogallab.mobile.ui.navigation

sealed interface NavEvent {
   // targets to navigate to
   data object ToPersonInput : NavEvent
   data class  ToPersonDetail(val id: String) : NavEvent
   data object ToPeopleList : NavEvent
}
