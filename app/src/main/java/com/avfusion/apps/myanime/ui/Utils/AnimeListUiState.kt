package com.avfusion.apps.myanime.ui.Utils

import com.avfusion.apps.myanime.data.model.AnimeEntity

sealed class AnimeListUiState {
    object Loading : AnimeListUiState()
    data class Success(val data: List<AnimeEntity>) : AnimeListUiState()
    object ErrorNoDataAndNoNetwork : AnimeListUiState()
}