package com.example.frogs.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        // Notes: Commented out code, also ok instead of CircularProgressIndicator
        //Image(
        //    modifier = Modifier.size(200.dp),
        //    painter = painterResource(R.drawable.loading_img),
        //    contentDescription = stringResource(R.string.loading)
        //)
        CircularProgressIndicator()
    }
}