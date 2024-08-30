package de.rogallab.mobile

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.rogallab.mobile.ui.composables.InputName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InputNameTest {

   @get:Rule
   val composeTestRule = createAndroidComposeRule<MainActivity>()

   @OptIn(ExperimentalComposeUiApi::class) // Opting into the Experimental API
   @Test
   fun inputName_showsError_whenNameIsInvalid() {
      // Arrange
      val testName = ""
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
      composeTestRule.onNodeWithText("InputNameField")
         .performTextInput("") // Input an empty name to trigger validation
      // Simulate focus loss to trigger validation
      composeTestRule.onNodeWithText("InputNameField")
         .performClick() // Ensure the text field gains focus first
      composeTestRule.onNodeWithText("InputEMailField") // Another UI element to focus on
         .performClick() // Clicking elsewhere to cause focus loss on the text field

      // Assert that the error message is displayed
      composeTestRule.onNodeWithText(testErrorMessage)
         .assertExists("Failure message should be displayed when name is empty")

      // Assert
      composeTestRule.onNodeWithText(testErrorMessage)
         .assertExists("Failure message should be displayed when name is empty")
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
      composeTestRule.onNodeWithTag("InputNameField")
         .performTextInput(testName)

      // Simulate focus loss to trigger validation
      composeTestRule.onRoot()
         .performClick() // Click outside the text field to lose focus

      // Assert that the error message is not displayed
      composeTestRule.onNodeWithTag("ErrorText")
         .assertDoesNotExist()
   }

   @OptIn(ExperimentalComposeUiApi::class)
   @Test
   fun inputName_triggersValidationOnFocusLoss() {
      // Arrange
      val testName = "John"
      val testErrorMessage = "Name cannot be empty"
      var inputName = ""
      val onNameChange: (String) -> Unit = { inputName = it }
      val validateName: (String) -> Pair<Boolean, String> = { name ->
         if (name.isEmpty()) Pair(true, testErrorMessage) else Pair(false, "")
      }

      // Act
      composeTestRule.setContent {
         InputName(
            name = inputName,
            onNameChange = onNameChange,
            validateName = validateName
         )
      }

      // Simulate the user typing in an empty name (which is invalid)
      composeTestRule.onNodeWithTag("InputNameField")
         .performTextInput("") // Input an empty name

      // Simulate focus loss by clicking elsewhere (this should trigger validation)
      composeTestRule.onRoot().performClick() // Click outside the text field to lose focus

      // Assert that the error message is displayed after losing focus
      composeTestRule.onNodeWithTag("ErrorText")
         .assertExists("Failure message should be displayed when name is invalid after focus loss")
   }

}
