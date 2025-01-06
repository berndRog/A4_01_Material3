package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PersonListItem(
   id: String,                // State ↓
   firstName: String,         // State ↓
   lastName: String,          // State ↓
   email: String,             // State ↓
   phone: String,             // State ↓
   onClicked: () -> Unit,     // Event ↑
   onDeleted: () -> Unit      // Event ↑
) {
   Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
         .background(MaterialTheme.colorScheme.inverseOnSurface)
   ) {
      Column(modifier = Modifier.padding(start = 4.dp).padding(vertical = 2.dp)
         .weight(0.9f)
         .clickable { onClicked() }
      ) {

         Text(
            text = "$firstName $lastName",
            style = MaterialTheme.typography.bodyLarge
         )
         Text(
            text = email,
            style = MaterialTheme.typography.bodySmall
         )
         Text(
            text = phone,
            style = MaterialTheme.typography.bodySmall
         )
      } // Column

      IconButton(
         onClick = { onDeleted() }, // Event ↑
         modifier = Modifier.padding(end = 4.dp)
            .weight(0.1f)
      ) {
         Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Delete item"
         )
      }
   } // Row
} // PersonListItem
