package com.example.androidlastassignment.repository

import android.content.Context
import androidx.room.Room
import com.example.androidlastassignment.database.PersonEntityDatabase
import com.example.androidlastassignment.model.Person
import com.example.androidlastassignment.model.PersonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonEntityRepository(context: Context) {

    private val database = Room.databaseBuilder(
        context,
        PersonEntityDatabase::class.java,
        "personEntity_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val personEntityDao = database.PersonEntityDao()

    val personEntityList = personEntityDao.getPersonEntity()

    suspend fun insertPerson(person: Person) {
        val personEntity = PersonEntity(
            title = person.name.title,
            first = person.name.first,
            last = person.name.last,
            large = person.picture.large,
            medium = person.picture.medium,
            thumbnail = person.picture.thumbnail
        )
        withContext(Dispatchers.IO) {
            personEntityDao.insertPersonEntity(personEntity)
        }
    }

    suspend fun deletePerson(person: Person) {
        val personEntity = PersonEntity(
            title = person.name.title,
            first = person.name.first,
            last = person.name.last,
            large = person.picture.large,
            medium = person.picture.medium,
            thumbnail = person.picture.thumbnail
        )
        withContext(Dispatchers.IO) {
            personEntityDao.deletePersonEntity(personEntity)
        }
    }

    suspend fun insertPersonEntity(personEntity: PersonEntity) {
        withContext(Dispatchers.IO) {
            personEntityDao.insertPersonEntity(personEntity)
        }
    }

    suspend fun deletePersonEntity(personEntity: PersonEntity) {
        withContext(Dispatchers.IO) {
            personEntityDao.deletePersonEntity(personEntity)
        }
    }
}