package com.avfusion.apps.myanime.ui.Utils

import com.avfusion.apps.myanime.data.model.AnimeDetailsEntity
import com.avfusion.apps.myanime.data.model.CharacterEntity

sealed class AnimeDetailsUiState {
    object Loading : AnimeDetailsUiState()
    data class Success(
        val animeDetails: AnimeDetailsEntity,
        val characters: List<CharacterEntity>
    ) : AnimeDetailsUiState()
    object ErrorNoDataAndNoNetwork : AnimeDetailsUiState()
}