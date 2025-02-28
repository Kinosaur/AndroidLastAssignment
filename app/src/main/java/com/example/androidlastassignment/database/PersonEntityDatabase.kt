package com.example.androidlastassignment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidlastassignment.dao.PersonEntityDao
import com.example.androidlastassignment.model.PersonEntity

@Database(entities = [PersonEntity::class] , version = 1)
abstract class PersonEntityDatabase: RoomDatabase() {
    abstract fun PersonEntityDao() : PersonEntityDao
}