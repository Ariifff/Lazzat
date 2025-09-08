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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.arif.lazzat.api.RecipeRepository
import com.arif.lazzat.ui.theme.darkcolor
import com.arif.lazzat.ui.theme.primarycolor
import com.arif.lazzat.viewmodel.RecipeDetailViewModel
import com.arif.lazzat.viewmodel.RecipeDetailViewModelFactory



@Composable
fun RecipeDetailScreen(
    id: String,
    title: String,
    image: String,
) {
    val repository = remember { RecipeRepository(com.arif.lazzat.api.RetrofitInstance.api) }

    val viewModel: RecipeDetailViewModel = viewModel(
        factory = RecipeDetailViewModelFactory(repository)
    )

    val isLoading by viewModel.isLoading.observeAsState(false)

    val details by viewModel.recipeDetails.observeAsState()
    val context = LocalContext.current


    // fetch details when screen opens
    LaunchedEffect(id) {
        viewModel.fetchRecipeDetails(id.toInt())
    }

    val scrollState = rememberScrollState()

    if (isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {

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

            // ✅ Summary from API
            Text(
                "Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            details?.summary?.let { safeSummary ->
                AndroidView(
                    factory = { context ->
                        TextView(context).apply {
                            text = Html.fromHtml(safeSummary, Html.FROM_HTML_MODE_LEGACY)
                            textSize = 16f
                            setLineSpacing(1.2f, 1.2f)
                            linksClickable = true
                            movementMethod = android.text.method.LinkMovementMethod.getInstance()
                        }
                    },
                    update = { textView ->
                        textView.text = Html.fromHtml(safeSummary, Html.FROM_HTML_MODE_LEGACY)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ingredients Section
            Text(
                "Ingredients",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            details?.extendedIngredients?.forEach { ing ->
                Text("• ${ing.original}", style = MaterialTheme.typography.bodyMedium)
            } ?: Text("Loading ingredients...")

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ Instructions from API
            Text(
                "Instructions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            details?.instructions?.let { safeInstructions ->
                AndroidView(
                    factory = { context ->
                        TextView(context).apply {
                            text = Html.fromHtml(safeInstructions, Html.FROM_HTML_MODE_LEGACY)
                            textSize = 16f
                            setLineSpacing(1.2f, 1.2f)
                            linksClickable = true
                            movementMethod = android.text.method.LinkMovementMethod.getInstance()


                        }
                    },
                    update = { textView ->
                        textView.text = Html.fromHtml(safeInstructions, Html.FROM_HTML_MODE_LEGACY)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkcolor),
                onClick = {
                    val query = title
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/results?search_query=$query recipe")
                    )
                    context.startActivity(intent)
                }) {
                Text("Watch on YouTube")
            }
        }
    }
}

