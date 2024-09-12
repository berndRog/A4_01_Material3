package de.rogallab.mobile

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import de.rogallab.mobile.ui.composables.InputName
import org.junit.Rule
import org.junit.Test

class InputNameTest {

   @get:Rule
   val composeTestRule = createComposeRule()

   @OptIn(ExperimentalComposeUiApi::class)
   @Test
   fun inputName_showsError_whenNameIsInvalid() {
      // Arrange
      val testName = "B"
      val testErrorMessage = "Name cannot be empty"
      val onNameChange: (String) -> Unit = {}
      val validateName: (String) -> Pair<Boolean, String> = { name ->
         if (name.isEmpty()) Pair(true, testErrorMessage) else Pair(false, "")
      }

      // Act
      composeTestRule.setContent {
         InputName(
            name = testName,
            onNameChange = onNameChange,
            validateName = validateName
         )
      }

      // Simulate the user typing in the text field and losing focus
      composeTestRule.onNodeWithTag("InputName")
         .performTextInput(testName) // Input a name with one character to trigger validation
      // Simulate focus loss to trigger validation
//      composeTestRule.onNodeWithTag("InputName")
//         .performClick() // Ensure the text field gains focus first
      composeTestRule.onRoot().performClick() // Click outside the text field to lose focus

      // Assert that the error message is displayed
      composeTestRule.onNodeWithTag("ErrorText")
         .assertExists("Error message should be displayed when name is empty")
   }

   @OptIn(ExperimentalComposeUiApi::class)
   @Test
   fun inputName_hidesError_whenNameIsValid() {
      // Arrange
      val testName = "John Doe"
      val onNameChange: (String) -> Unit = {}
      val validateName: (String) -> Pair<Boolean, String> = { name ->
         if (name.isEmpty()) Pair(true, "Name cannot be empty") else Pair(false, "")
      }

      // Act
      composeTestRule.setContent {
         InputName(
            name = testName,
            onNameChange = onNameChange,
            validateName = validateName
         )
      }

      // Simulate the user typing in a valid name
      composeTestRule.onNodeWithTag("InputName")
         .performTextInput(testName)

      // Simulate focus loss to trigger validation
      composeTestRule.onRoot().performClick() // Click outside the text field to lose focus

      // Assert that the error message is not displayed
      composeTestRule.onNodeWithTag("ErrorText")
         .assertDoesNotExist()
   }
}