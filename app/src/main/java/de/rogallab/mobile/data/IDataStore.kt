package de.rogallab.mobile.data

import de.rogallab.mobile.domain.entities.Person

interface IDataStore {
   fun selectAll(): List<Person>
   fun selectWhere(predicate: (Person) -> Boolean): List<Person>
   fun findById(id: String): Person?
   fun findBy(predicate: (Person) -> Boolean): Person?

   fun insert(person: Person)
   fun update(person: Person)
   fun delete(person: Person)
}