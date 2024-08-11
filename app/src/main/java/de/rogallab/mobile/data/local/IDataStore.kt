package de.rogallab.mobile.data.local

import de.rogallab.mobile.domain.entities.Person

interface IDataStore {
   fun selectAll()
      : MutableList<Person>
   fun selectWhere(predicate: (Person) -> Boolean)
      : MutableList<Person>
   fun findById(id: String)
      : Person?
   fun findBy(predicate: (Person) -> Boolean)
      : Person?

   fun insert(person: Person)
   fun update(personToUpdate: Person)
   fun delete(id: String)

   fun readDataStore()
   fun writeDataStore()
}