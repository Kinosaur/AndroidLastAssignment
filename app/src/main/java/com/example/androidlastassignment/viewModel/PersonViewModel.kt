package com.example.androidlastassignment.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidlastassignment.httpClient
import com.example.androidlastassignment.model.Person
import com.example.androidlastassignment.model.PersonEntity
import com.example.androidlastassignment.model.ResultWrapper
import com.example.androidlastassignment.repository.ImageRepository
import com.example.androidlastassignment.repository.PersonEntityRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore(name = "image_store")

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonEntityRepository(application)
    private val imageRepository = ImageRepository(application)

    val personEntityList = personRepository.personEntityList

    private val _personList = MutableStateFlow<List<Person>>(emptyList())
    val personList: StateFlow<List<Person>> = _personList

    private val _imageList = MutableStateFlow<List<String>>(emptyList())
    val imageList: StateFlow<List<String>> = _imageList

    init {
        loadPerson()
        loadSavedImages()
    }

    fun loadPerson() {
        val url = "https://randomuser.me/api"
        viewModelScope.launch {
            val data = httpClient.get(url).body<ResultWrapper>()
            val person = data.results.first()

            _personList.value += person

            savePerson(person)
        }
    }

    fun savePerson(person: Person) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personRepository.insertPerson(person)
            }
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personRepository.deletePerson(person)
            }
        }
    }

    fun deletePersonEntity(personEntity: PersonEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                personRepository.deletePersonEntity(personEntity)
            }
        }
    }

    fun saveImage(imageUri: String) {
        viewModelScope.launch {
            Log.d("PersonViewModel", "Saving image URI: $imageUri")
            imageRepository.saveImage(imageUri)
            loadSavedImages() // Reload images after saving
        }
    }

    fun deleteImage(imageUri: String) {
        viewModelScope.launch {
            imageRepository.deleteImage(imageUri)
            loadSavedImages()
        }
    }

    fun loadSavedImages() {
        viewModelScope.launch {
            val savedImages = imageRepository.loadSavedImages()
            _imageList.value = savedImages
            Log.d("PersonViewModel", "Images loaded into _imageList: $savedImages") // Log loaded images
        }
    }
}