package de.rogallab.mobile.data.local

import android.content.Context
import de.rogallab.mobile.data.IDataStore
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.logDebug
import de.rogallab.mobile.domain.utilities.logError
import de.rogallab.mobile.domain.utilities.logVerbose
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class DataStore(
   private val _context: Context
): IDataStore {

   // list of people
   private var _people: MutableList<Person> = mutableListOf()

   // Json serializer
   private val _json = Json {
      prettyPrint = true
      ignoreUnknownKeys = true
   }

   init {
      logDebug(TAG, "init: read datastore")
      _people.clear()
      read()
   }

   override fun selectAll(): List<Person> =
      _people.toList()

   override fun selectWhere(predicate: (Person) -> Boolean): List<Person> =
      _people.filter(predicate).toList()

   override fun findById(id: String): Person? =
      _people.firstOrNull{ it:Person -> it.id == id }

   override fun findBy(predicate: (Person) -> Boolean): Person? =
      _people.firstOrNull(predicate)

   override fun insert(person: Person) {
      logVerbose(TAG, "insert: $person")
      if (_people.any { it.id == person.id })
         throw IllegalArgumentException("Person with id ${person.id} already exists")
      _people.add(person)
      write()
   }

   override fun update(person: Person) {
      logVerbose(TAG, "update: $person")
      val index = _people.indexOfFirst { it.id == person.id }
      if (index == -1)
         throw IllegalArgumentException("Person with id ${person.id} does not exist")
      _people[index] = person
      write()
   }

   override fun delete(person: Person) {
      logVerbose(TAG, "delete: $person")
      if (_people.none { it.id == person.id })
         throw IllegalArgumentException("Person with id ${person.id} does not exist")
      _people.remove(person)
      write()
   }

   // dataStore is saved as JSON file to the user's home directory
   // UserHome/Documents/android/people.json
   private fun read() {
      try {
         val filePath = getFilePath(FILE_NAME)
         // if file does not exist or is empty, return an empty list
         val file = File(filePath)
         if (!file.exists() || file.readText().isBlank()) {
            // seed _people with some data
            seedData()
            logVerbose(TAG, "read: seedData people:${_people.size}")
            write()
            return
         }
         // read json from a file and convert to a list of people
         val jsonString = File(filePath).readText()
         _people = _json.decodeFromString(jsonString)

         logVerbose(TAG, jsonString)
      } catch (e: Exception) {
         logError(TAG, "Failed to read: ${e.message}")
         throw e
      }
   }

   // write the list of people to the dataStore
   private fun write() {
      try {
         val filePath = getFilePath(FILE_NAME)
         val jsonString = _json.encodeToString(_people)
         // save to a file
         val file = File(filePath)
         file.writeText(jsonString)
         logVerbose(TAG, "write: $jsonString")
      } catch (e: Exception) {
         logError(TAG, "Failed to write: ${e.message}")
         throw e
      }
   }

   // get the file path for the dataStore
   // UserHome/Documents/android/people.json
   private fun getFilePath(fileName: String): String {
      try {
         // get the Apps home directory
         val appHome = _context.filesDir
         // the directory must exist, if not create it
         val directoryPath = "$appHome/documents/$DIRECTORY_NAME"
         if ( !directoryExists(directoryPath) )
            createDirectory(directoryPath)
         // return the file path
         return "$directoryPath/$fileName"
      } catch (e: Exception) {
         logError(TAG, "Failed to getFilePath or create directory; ${e.localizedMessage}")
         throw e
      }
   }

   private fun directoryExists(directoryPath: String): Boolean {
      val directory = File(directoryPath)
      return directory.exists() && directory.isDirectory
   }

   private fun createDirectory(directoryPath: String): Boolean {
      val directory = File(directoryPath)
      return directory.mkdirs()
   }

   private fun seedData() {
      val firstNames = mutableListOf(
         "Arne", "Berta", "Cord", "Dagmar", "Ernst", "Frieda", "Günter", "Hanna",
         "Ingo", "Johanna", "Klaus", "Luise", "Martin", "Nadja", "Otto", "Patrizia",
         "Quirin", "Rebecca", "Stefan", "Tanja", "Uwe", "Veronika", "Walter", "Xaver",
         "Yvonne", "Zwantje")
      val lastNames = mutableListOf(
         "Arndt", "Bauer", "Conrad", "Diehl", "Engel", "Fischer", "Graf", "Hoffmann",
         "Imhoff", "Jung", "Klein", "Lang", "Meier", "Neumann", "Olbrich", "Peters",
         "Quart", "Richter", "Schmidt", "Thormann", "Ulrich", "Vogel", "Wagner", "Xander",
         "Yakov", "Zander")
//      val random = Random(0)
      for (index in firstNames.indices) {
//         var indexFirst = random.nextInt(firstNames.size)
//         var indexLast = random.nextInt(lastNames.size)
         val firstName = firstNames[index]
         val lastName = lastNames[index]
         val person = Person(firstName, lastName)
         _people.add(person)
      }
   }

   companion object {
      private const val TAG = "<-DataStore"
      private const val DIRECTORY_NAME = "android"
      private const val FILE_NAME = "people1.json"
   }
}