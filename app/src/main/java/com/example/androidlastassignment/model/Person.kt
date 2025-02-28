package com.example.androidlastassignment.model

import kotlinx.serialization.Serializable

@Serializable
data class Person (
    val name : Name = Name(),
    val picture: Picture = Picture()
)