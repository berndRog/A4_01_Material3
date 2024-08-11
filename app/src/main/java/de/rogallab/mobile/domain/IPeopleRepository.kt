package de.rogallab.mobile.domain

import de.rogallab.mobile.domain.entities.Person

interface IPeopleRepository {

    fun getAll()
       : ResultData<MutableList<Person>>
    fun getWhere(predicate: (Person) -> Boolean)
       : ResultData<MutableList<Person>>
    fun findById(id: String)
       : ResultData<Person?>
    fun findBy(predicate: (Person) -> Boolean)
       : ResultData<Person?>

    fun create(person: Person): ResultData<Unit>
    fun update(person: Person): ResultData<Unit>
    fun remove(id: String): ResultData<Unit>

    fun readDataStore(): ResultData<Unit>
    fun writeDataStore(): ResultData<Unit>

}