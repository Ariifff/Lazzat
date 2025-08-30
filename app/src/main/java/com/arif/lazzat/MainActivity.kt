package com.arif.lazzat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arif.lazzat.navigation.MainNavGraph
import com.arif.lazzat.ui.theme.LazzatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazzatTheme {
                MainNavGraph()
            }
        }
    }
}