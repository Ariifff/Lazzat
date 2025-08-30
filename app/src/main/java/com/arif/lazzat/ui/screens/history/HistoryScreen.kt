package com.arif.lazzat.ui.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(
    padding: PaddingValues,
    onFilterClick: () -> Unit,
    onDetailClick: () -> Unit
) {
    Column(modifier = Modifier.padding(padding)) {
        // Filter button
        Button(onClick = onFilterClick) {
            Text("Filter History")
            Icon(Icons.Default.List, contentDescription = "Filter")
        }

        // History items list
        LazyColumn {
            items(10) { index ->
                Card(
                    onClick = onDetailClick,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("History Item $index", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}