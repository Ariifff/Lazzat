import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arif.lazzat.api.RecipeItem
import com.arif.lazzat.api.RecipeRepository
import com.arif.lazzat.api.RetrofitInstance.api
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<RecipeItem>>()
    val recipes: LiveData<List<RecipeItem>> = _recipes


    fun searchRecipes(
        ingredients: String? = null,
        cuisines: List<String>? = null,
        diets: List<String>? = null,
    ) {
        viewModelScope.launch {
            try {
                val finalIngredients = ingredients?.ifEmpty { null }
                val finalCuisines = cuisines?.ifEmpty { null }
                val finalDiets = diets?.ifEmpty { null }

                val response = api.searchRecipes(
                    ingredients = finalIngredients,
                    cuisines = finalCuisines?.joinToString(","),
                    diets = finalDiets?.joinToString(","),
                    apiKey = "1743fe933df34554a8babfa88594d6c2"
                )

                _recipes.value = response.results
                Log.d("API_TEST", "Response: $response")

            } catch (e: Exception) {
                Log.e("API_TEST", "Error: ${e.message}")
            }
        }
    }
}
