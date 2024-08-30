package de.rogallab.mobile.data.local

import android.content.Context
import de.rogallab.mobile.domain.entities.Person
import de.rogallab.mobile.domain.utilities.logError
import de.rogallab.mobile.domain.utilities.logVerbose
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.random.Random

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

   override fun selectAll(): MutableList<Person> =
      _people

   override fun selectWhere(predicate: (Person) -> Boolean): MutableList<Person> =
      _people.filter(predicate).toMutableList()

   override fun findById(id: String): Person? =
      _people.firstOrNull{ it:Person -> it.id == id }


   override fun findBy(predicate: (Person) -> Boolean): Person? =
      _people.firstOrNull(predicate)

   override fun insert(person: Person) {
      logVerbose(TAG, "insert: $person")
      if (!_people.any { it.id == person.id }) {
         _people.add(person)
      }
   }

   override fun update(personToUpdate: Person) {
      logVerbose(TAG, "update: $personToUpdate")
      val index = _people.indexOfFirst { it.id == personToUpdate.id }
      if (index != -1) {
         _people[index] = personToUpdate
      }
   }

   override fun delete(id: String) {
      _people.removeIf { it.id == id }
   }

   override fun readDataStore() {
      logVerbose(TAG, "readDataStore()")
      _people.clear()
      // read data from the dataStore
      read()
   }

   override fun writeDataStore() {
      logVerbose(TAG, "writeDataStore()")
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
            logVerbose(TAG, "readDataStore: seedData people:${_people.size}")
            write()
            return
         }
         // read json from a file and convert to a list of people
         val jsonString = File(filePath).readText()
         logVerbose(TAG, "readDataStore: $jsonString")
         _people = _json.decodeFromString(jsonString)
         logVerbose(TAG, "readDataStore: ${_people.size}")
      } catch (e: Exception) {
         logError(TAG, "Failed to read dataStore; ${e.localizedMessage}")
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
         logVerbose(TAG, "writeDataStore: $jsonString")
      } catch (e: Exception) {
         logError(TAG, "Failed to write dataStore; ${e.localizedMessage}")
         throw e
      }
   }

   // get the file path for the dataStore
   // UserHome/Documents/android/people.json
   private fun getFilePath(fileName: String): String {
      try {
         // get the user's home directory
         // val userHome = System.getProperty("user.home")
         // get the Apps home directory
         val appHome = _context.filesDir
         // the directory UserHome/Documents must exist
         // check if the directory exists, if not create it
         val directoryPath = "$appHome/documents/$DIRECTORY_NAME"
         if (!directoryExists(directoryPath)) {
            createDirectory(directoryPath)
         }
         // return the file path
         val filePath = "$directoryPath/$fileName"
         return filePath
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
      val emailProvider = mutableListOf("gmail.com", "icloud.com", "outlook.com", "yahoo.com",
         "t-online.de", "gmx.de", "freenet.de", "mailbox.org", "yahoo.com", "web.de")

      val random = Random(0)
      for (index in firstNames.indices) {
//         var indexFirst = random.nextInt(firstNames.size)
//         var indexLast = random.nextInt(lastNames.size)
         val firstName = firstNames[index]
         val lastName = lastNames[index]
         val email =
            "${firstName.lowercase()}." +
               "${lastName.lowercase()}@" +
               "${emailProvider.random()}"
         val phone =
            "0${random.nextInt(1234, 9999)} " +
               "${random.nextInt(100, 999)}-" +
               "${random.nextInt(10, 9999)}"

         val person = Person(firstName, lastName, email, phone)
         _people.add(person)
      }
   }


   companion object {
      private const val TAG = "[DataStore]"
      private const val DIRECTORY_NAME = "android"
      private const val FILE_NAME = "people.json"
   }
}