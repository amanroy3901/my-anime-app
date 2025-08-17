package com.avfusion.apps.myanime.ui.screens.topanime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.avfusion.apps.myanime.ui.Utils.AnimeListUiState
import com.avfusion.apps.myanime.ui.components.AnimeItemCard
import com.avfusion.apps.myanime.ui.navigation.Screen
import com.avfusion.apps.myanime.ui.screens.NoDataAndNoNetworkScreen

@Composable
fun AnimeListScreen(
    viewModel: AnimeViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyGridState()

    val endOfListReached by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemCount = listState.layoutInfo.totalItemsCount
            if (uiState !is AnimeListUiState.Loading) {
                lastVisibleItemIndex >= totalItemCount - 3
            } else {
                false
            }
        }
    }

    LaunchedEffect(endOfListReached) {
        if (endOfListReached) {
            viewModel.fetchNextPage()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Text(
            text = "Top Anime",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        when (uiState) {
            is AnimeListUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
            is AnimeListUiState.Success -> {
                val animeList = (uiState as AnimeListUiState.Success).data
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(animeList) { anime ->
                        AnimeItemCard(anime = anime,
                            onItemClick = {
                                navController.navigate(Screen.AnimeDetails.createRoute(anime.id))
                            })
                    }
                }
            }
            is AnimeListUiState.ErrorNoDataAndNoNetwork -> {
                NoDataAndNoNetworkScreen(onRetry = { viewModel.fetchAnime() })
            }
        }
    }
}