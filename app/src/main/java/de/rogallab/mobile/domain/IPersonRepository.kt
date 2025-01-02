package de.rogallab.mobile.domain

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.rogallab.mobile.domain.entities.Person

interface IPeopleRepository {

    fun getAll(): ResultData<List<Person>>
    fun getWhere(predicate: (Person) -> Boolean): ResultData<List<Person>>
    fun getById(id: String): ResultData<Person?>
    fun getBy(predicate: (Person) -> Boolean): ResultData<Person?>

    fun create(person: Person): ResultData<Unit>
    fun update(person: Person): ResultData<Unit>
    fun remove(person: Person): ResultData<Unit>

}

@Composable
fun xyz() {

    Surface(
       shape = MaterialTheme.shapes.medium,
       color = MaterialTheme.colorScheme.surface,
       shadowElevation = 4.dp
    ) {
        Text("Inhalt auf einer Material-Oberfläche")
    }

    Row {
        Text("Element 1")
        Text("Element 2")
    }

    Column {
        Text("Element 1")
        Text("Element 2")
    }

}
