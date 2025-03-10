package com.example.androidlastassignment.view

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidlastassignment.viewModel.PersonViewModel

@Composable
fun MainAppScreen(modifier : Modifier){

    val personViewModel: PersonViewModel = viewModel()
    val personEntityList = personViewModel.personEntityList.observeAsState()
    val imageList = personViewModel.imageList.collectAsState()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "PersonList"
    ){

        composable("PersonList"){
            PersonListScreen(
                modifier, personEntityList.value.orEmpty(),
                loadPersonClicked = {personViewModel.loadPerson()},
                viewGalleryClicked = { navController.navigate("Gallery")},
                saveClicked = {personViewModel.saveImage(it)},
                personEntityDelete = {personViewModel.deletePersonEntity(it)},
                imageDelete = {personViewModel.deleteImage(it)}
            )
        }

        composable("Gallery") {
            GalleryScreen(
                imageList.value,
                modifier,
                imageClicked = { image ->
                    val encodedImage = Uri.encode(image) // Encode URL properly
                    navController.navigate("ImageDetail/$encodedImage")
                }
            )
        }

        composable(
            "ImageDetail/{image}",
            arguments = listOf(navArgument("image") { type = NavType.StringType })
        ) { backStackEntry ->
            val image = backStackEntry.arguments?.getString("image")?.let { Uri.decode(it) } ?: ""
            ImageDetailScreen(
                image = image,
                modifier = modifier
            )
        }

    }

}