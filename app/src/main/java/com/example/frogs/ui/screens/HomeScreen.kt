package com.example.frogs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.frogs.R
import com.example.frogs.model.FrogRec

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: FrogsViewModel,
    retryAction: () -> Unit = viewModel::getFrogRecords,
) {
    val uiState = viewModel.uiState.collectAsState().value
    when(uiState){
        is FrogsUiState.Loading -> LoadingScreen(modifier)
        is FrogsUiState.Success -> ListScreen(frogList = uiState.frogList, modifier = modifier)
        is FrogsUiState.Error -> ErrorScreen(retryAction = retryAction, modifier)
    }
}


@Composable
private fun ListScreen(frogList: List<FrogRec>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = frogList,
            key = {
                it.name
            }
        ) {
            ListItem(frogRec = it)
        }
    }
}

@Composable
private fun ListItem(frogRec: FrogRec, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        //border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = frogRec.type,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = frogRec.description,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            )
            Text(
                text = frogRec.name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Notes: The code commented out also works. just a different way of doing it
            var showProgress by rememberSaveable { mutableStateOf(false) }
            //var showError by rememberSaveable { mutableStateOf(false) }
            if (showProgress){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    //if (showError){
                    //    Image(
                    //        modifier = Modifier.size(50.dp),
                    //        imageVector = Icons.Default.Warning,
                    //        contentDescription = null,
                    //        colorFilter = ColorFilter.tint(Color.Red)
                    //    )
                    //} else {
                    CircularProgressIndicator()
                    //}
                }
            }
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(frogRec.imgSrc)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                error = painterResource(id = R.drawable.ic_broken_image),
                onError = {
                    showProgress = false
                    //showError = true
                },
                onLoading = {
                    showProgress = true
                    //showError = false
                },
                onSuccess = {
                    showProgress = false
                    //showError = false
                },
                //placeholder = painterResource(id = R.drawable.loading_img),
            )
        }
    }
}
