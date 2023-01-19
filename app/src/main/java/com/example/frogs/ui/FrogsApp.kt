package com.example.frogs.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.frogs.ui.screens.FrogsViewModel
import com.example.frogs.ui.screens.HomeScreen

@Composable
fun FrogsApp(
    viewModel: FrogsViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar()
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            //Text(text = "WTF Hello")
            HomeScreen(viewModel = viewModel)
        }
    }
}