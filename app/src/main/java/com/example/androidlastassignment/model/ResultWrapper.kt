package com.example.androidlastassignment.model

import kotlinx.serialization.Serializable

@Serializable
data class ResultWrapper (
    val results: List<Person> = listOf()
)