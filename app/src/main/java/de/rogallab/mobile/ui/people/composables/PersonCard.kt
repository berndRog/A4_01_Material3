package de.rogallab.mobile.ui.people.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PersonCard(
   firstName: String,
   lastName: String,
   email: String?,
   phone: String?,
   imagePath: String?,
   modifier: Modifier = Modifier
) {
   Card(
      modifier = modifier.fillMaxWidth(),
      shape = RoundedCornerShape(percent = 10),
   ) {
      Row(
         verticalAlignment = Alignment.Companion.CenterVertically,
      ) {
         Column(
            modifier = Modifier
               .weight(0.85f)
               .padding(vertical = 4.dp)
               .padding(horizontal = 8.dp)
         ) {
            Text(
               text = "$firstName $lastName",
               style = MaterialTheme.typography.bodyLarge,
            )
            email?.let {
               Text(
                  text = it,
                  style = MaterialTheme.typography.bodyMedium
               )
            }
            phone?.let {
               Text(
                  text = phone,
                  style = MaterialTheme.typography.bodyMedium,
               )
            }
         }
      }
   }
}