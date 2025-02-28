package com.example.androidlastassignment.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "image_store")

class ImageRepository(private val context: Context) {

    private val IMAGE_LIST_KEY = stringPreferencesKey("image_list")

    suspend fun saveImage(imageUri: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[IMAGE_LIST_KEY]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableList() ?: mutableListOf()

            // Avoid saving duplicates
            if (!currentList.contains(imageUri)) {
                currentList.add(imageUri)
                preferences[IMAGE_LIST_KEY] = currentList.joinToString(",")
                Log.d("ImageRepository", "Image saved: $imageUri") // Log saved image URI
            } else {
                Log.d("ImageRepository", "Duplicate image, not saving: $imageUri")
            }
        }

        // Check if the images were actually saved
        val savedImages = loadSavedImages()
        Log.d("ImageRepository", "Saved images after saving: $savedImages")
    }


    suspend fun deleteImage(imageUri: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[IMAGE_LIST_KEY]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableList() ?: mutableListOf()

            if (currentList.contains(imageUri)) {
                currentList.remove(imageUri)
                preferences[IMAGE_LIST_KEY] = currentList.joinToString(",")
            }
        }
    }

    suspend fun loadSavedImages(): List<String> {
        val savedImages = context.dataStore.data
            .map { preferences ->
                preferences[IMAGE_LIST_KEY]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
            }
            .first()

        Log.d("ImageRepository", "Loaded Images: $savedImages") // Log the loaded images
        return savedImages
    }

}