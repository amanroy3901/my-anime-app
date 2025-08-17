package com.avfusion.apps.myanime.ui.screens.animedetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avfusion.apps.myanime.data.model.AnimeDetailsEntity
import com.avfusion.apps.myanime.data.model.CharacterEntity
import com.avfusion.apps.myanime.data.repository.AnimeRepository
import com.avfusion.apps.myanime.ui.Utils.AnimeDetailsUiState
import com.avfusion.apps.myanime.utility.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = savedStateHandle.get<Int>("animeId") ?: -1

    private val _uiState = MutableStateFlow<AnimeDetailsUiState>(AnimeDetailsUiState.Loading)
    val uiState: StateFlow<AnimeDetailsUiState> = _uiState.asStateFlow()

    private val detailsFlow = repository.getAnimeDetails(animeId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val charactersFlow = repository.getAnimeCharacters(animeId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var showVoiceActorImage by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            combine(
                detailsFlow,
                charactersFlow,
                networkMonitor.isOnline
            ) { details, characters, isOnline ->
                if (details == null) {
                    if (isOnline) {
                        AnimeDetailsUiState.Loading
                    } else {
                        AnimeDetailsUiState.ErrorNoDataAndNoNetwork
                    }
                } else {
                    AnimeDetailsUiState.Success(details, characters)
                }
            }.collect { state ->
                _uiState.value = state

                if (state is AnimeDetailsUiState.Loading) {
                    fetchData()
                }
            }
        }
    }

    fun fetchData() {
        if (animeId != -1) {
            viewModelScope.launch {
                repository.fetchAndSyncAnimeDetails(animeId)
                repository.fetchAndSyncAnimeCharacters(animeId)
            }
        }
    }

    fun toggleVoiceActorImage() {
        showVoiceActorImage = !showVoiceActorImage
    }
}