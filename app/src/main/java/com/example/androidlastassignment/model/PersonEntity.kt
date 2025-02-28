package com.example.androidlastassignment.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PersonEntity")
data class PersonEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val first : String,
    val last : String,
    val large : String,
    val medium : String,
    val thumbnail : String
)