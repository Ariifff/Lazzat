package com.arif.lazzat.ui.screens.home

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.arif.lazzat.api.RecipeRepository
import com.arif.lazzat.viewmodel.RecipeDetailViewModel
import com.arif.lazzat.viewmodel.RecipeDetailViewModelFactory

/*
@Preview(showBackground = true)
@Composable
fun recipeshow(){
    RecipeDetailScreen(
        id= "1",
        title = "Chicken Alfredo",
        image = "https://www.themealdb.com/images/media/meals/sytuqu1511553755.jpg",
        summary = "This is a delicious chicken alfredo recipe.",

    )
}
*/

@Composable
fun RecipeDetailScreen(
    id: String,
    title: String,
    image: String,
    summary: String,
) {

    val repository = remember { RecipeRepository(com.arif.lazzat.api.RetrofitInstance.api) }

    val viewModel: RecipeDetailViewModel = viewModel(
        factory = RecipeDetailViewModelFactory(repository)
    )

    val details by viewModel.recipeDetails.observeAsState()

    // fetch details when screen opens
    LaunchedEffect(id) {
        viewModel.fetchRecipeDetails(id.toInt())
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Recipe Image
        Image(
            painter = rememberAsyncImagePainter(model = image),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        AndroidView(
            factory = { context ->
                TextView(context).apply {
                    text = Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY)
                    textSize = 16f
                    setLineSpacing(1.2f, 1.2f) // better spacing
                    linksClickable = true
                    movementMethod = android.text.method.LinkMovementMethod.getInstance()
                }
            },
            update = { textView ->
                textView.text = Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY)
            }
        )


        Spacer(modifier = Modifier.height(16.dp))


        // Ingredients Section
        Text("Ingredients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        details?.extendedIngredients?.forEach { ing ->
            Text("• ${ing.original}", style = MaterialTheme.typography.bodyMedium)
        } ?: Text("Loading ingredients...")

        Spacer(modifier = Modifier.height(12.dp))

        /*
        Spacer(modifier = Modifier.height(16.dp))

        // Instructions
        Text("Instructions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        instructions.forEachIndexed { index, step ->
            Text("${index + 1}. $step", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // YouTube Link Button
        if (youtubeUrl != null) {
            val context = LocalContext.current
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Watch on YouTube")
            }
        }
        */
    }
}
