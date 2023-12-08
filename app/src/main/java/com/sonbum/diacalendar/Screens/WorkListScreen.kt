package com.sonbum.diacalendar.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun WorkListScreen() {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.secondaryVariant)
    ) {
      DiaGridView()


    }

}
@Composable
fun DiaRowView() {

    Row(modifier = Modifier
        .fillMaxSize()
        .padding(all = 2.dp)
    ) {
        Text(text = "1")
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "08:00")
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "08:30-10:00")
        Spacer(modifier = Modifier.width(100.dp))
        Text(text = "14:00#15:00")

    }
}

@Composable
fun DiaGridView(){
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        content = {
            items(100) { i -> 
                Box( modifier = Modifier
                    .padding(1.dp)
                    .aspectRatio(1f)
                    .clip(RectangleShape)
                    .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Item $i")
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dropdowntype() {
    val tabletype = listOf<String>("평일","휴일","평평","평휴","휴평","휴휴","교번")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(tabletype[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded ,
        onExpandedChange = {expanded = !expanded})
    {
        TextField(
            value = selectedOptionText,
            onValueChange = {},
            readOnly = true,
            //label = { Text(text = "근무표")},
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false })
        {
            tabletype.forEach { selectedText ->
                DropdownMenuItem(onClick = {
                    selectedOptionText = selectedText
                    expanded = false
                }) {
                    Text(text = selectedText)

                }
            }

        }
        

    }
}

