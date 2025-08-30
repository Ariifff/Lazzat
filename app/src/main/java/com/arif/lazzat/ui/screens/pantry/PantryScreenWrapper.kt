package com.arif.lazzat.ui.screens.pantry

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arif.lazzat.data.AppDatabase
import com.arif.lazzat.data.PantryItem
import com.arif.lazzat.ui.theme.lightcolor
import com.arif.lazzat.ui.theme.secondarycolor
import com.arif.lazzat.viewmodel.PantryFavouriteViewModel
import com.arif.lazzat.viewmodel.PantryFavouriteViewModelFactory

@Composable
fun PantryScreenWrapper(context: Context = LocalContext.current) {
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel: PantryFavouriteViewModel = viewModel(
        factory = PantryFavouriteViewModelFactory(db)
    )
    PantryScreen(viewModel)
}

@Composable
fun PantryScreen(viewModel: PantryFavouriteViewModel) {
    var newItemName by remember { mutableStateOf("") }
    val pantryList by viewModel.pantryItems.collectAsStateWithLifecycle(initialValue = emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        PantryTopSection()  // attractive top part
        Spacer(modifier = Modifier.height(16.dp))

        // New Item Input
        OutlinedTextField(
            value = newItemName,
            onValueChange = { newItemName = it },
            label = { Text("Enter item name", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (newItemName.isNotBlank()) {
                        viewModel.addPantryItem(newItemName.trim())
                        newItemName = ""
                        keyboardController?.hide()
                    }
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            viewModel.addPantryItem(newItemName.trim())
                            newItemName = ""
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pantry Items List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(pantryList, key = { it.id }) { item ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    PantryItemCard(
                        item = item,
                        onToggle = { updatedItem -> viewModel.updatePantryItem(updatedItem) },
                        onDelete = { viewModel.deletePantryItem(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun PantryTopSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        secondarycolor,
                        lightcolor
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Illustration or icon
            Icon(
                imageVector = Icons.Default.Star, // replace with basket or pantry icon
                contentDescription = "Pantry Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "My Pantry",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Keep your ingredients handy",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryItemCard(
    item: PantryItem,
    onToggle: (PantryItem) -> Unit,
    onDelete: (PantryItem) -> Unit
) {
    // Animate elevation when item is active
    val elevation by animateDpAsState(targetValue = if (item.isActive) 12.dp else 6.dp)
    val backgroundBrush = if (item.isActive) {
        Brush.horizontalGradient(
            colors = listOf(lightcolor, Color.White)
        )
    } else {
        Brush.linearGradient(colors = listOf(Color.White, Color.White))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 8.dp)
            .shadow(elevation, MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onToggle(item.copy(isActive = !item.isActive)) }, // toggle on card click
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // transparent to show brush
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Box(
            modifier = Modifier
                .background(backgroundBrush)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                // Toggle Switch with animation
                Switch(
                    checked = item.isActive,
                    onCheckedChange = { onToggle(item.copy(isActive = it)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Delete Icon with circular background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                        .clickable { onDelete(item) }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}



