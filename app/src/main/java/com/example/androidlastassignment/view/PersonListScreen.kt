package com.example.androidlastassignment.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidlastassignment.model.PersonEntity

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonListScreen(modifier : Modifier, personEntityList: List<PersonEntity>,
                     loadPersonClicked : () -> Unit,
                     viewGalleryClicked : () -> Unit,
                     personEntityDelete : (personEntity : PersonEntity) -> Unit,
                     imageDelete : (image : String) -> Unit,
                     saveClicked : (image : String) -> Unit ) {

    val groupedNames = personEntityList.groupBy{ it.first.first() }.toSortedMap()

    Column(modifier.padding(16.dp)) {
        LazyColumn(modifier = modifier.weight(1f)) {
            groupedNames.forEach { groupedName ->
                stickyHeader {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Text("Alphabet: ${groupedName.key}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
                items(groupedName.value) { item ->

                    val offsetX = remember { mutableFloatStateOf(0f) }
                    val leftThreshold = -50f  // Threshold to snap left
                    val rightThreshold = 50f  // Threshold to snap right
                    val maxLeftSwipe = -100f  // Maximum left swipe (reveal Delete)
                    val maxRightSwipe = 100f  // Maximum right swipe (reveal Save)

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(bottom = 16.dp)) {

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .background(Color.LightGray)){
                            Button(onClick = {
                                Log.d("PersonListScreen", "Save button clicked: ${item.large}")
                                saveClicked(item.large) // this will trigger saveImage in PersonViewModel
                                offsetX.floatValue = 0f
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green,
                                contentColor = Color.Black),
                                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.Start)
                                    .weight(1f)){
                                Text("Save")
                            }

                            Button(onClick = {
                                personEntityDelete(item)
                                imageDelete(item.large)
                                offsetX.floatValue = 0f
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red,
                                contentColor = Color.White),
                                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
                                    .weight(1f)){
                                Text("Delete")
                            }
                        }

                        //using z-index or putting last put this over everything in box
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                                .offset(offsetX.floatValue.dp)
                                .background(color = Color.White)
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { _, dragAmount ->
                                            offsetX.floatValue = (offsetX.floatValue + dragAmount).coerceIn(
                                                maxLeftSwipe,
                                                maxRightSwipe
                                            )
                                        },
                                        onDragEnd = {
                                            when {
                                                offsetX.floatValue < leftThreshold -> {
                                                    offsetX.floatValue = maxLeftSwipe
                                                }
                                                offsetX.floatValue > rightThreshold -> {
                                                    offsetX.floatValue = maxRightSwipe
                                                }
                                                else -> {
                                                    offsetX.floatValue = 0f
                                                }
                                            }
                                        }
                                    )
                                }
                        ) {
                            Text(
                                "${item.title} ${item.first} ${item.last}",
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxHeight()
                                    .wrapContentHeight(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                loadPersonClicked()
            }, modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Load Person", modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        Button(
            onClick = {
                viewGalleryClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text("View Gallery", modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}