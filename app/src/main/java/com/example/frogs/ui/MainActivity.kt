package com.example.frogs.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frogs.ui.screens.FrogsViewModel
import com.example.frogs.ui.theme.FrogsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrogsTheme {
                val viewModel : FrogsViewModel = viewModel(factory = FrogsViewModel.Factory)
                FrogsApp(viewModel)
            }
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    FrogsTheme {
//        Greeting("Android")
//    }
//}