package de.rogallab.mobile.ui.people

sealed class PeopleIntent {
   data object Fetch : PeopleIntent()
}