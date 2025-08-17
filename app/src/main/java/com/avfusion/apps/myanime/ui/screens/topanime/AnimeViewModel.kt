package com.avfusion.apps.myanime.ui.screens.topanime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avfusion.apps.myanime.data.repository.AnimeRepository
import com.avfusion.apps.myanime.ui.Utils.AnimeListUiState
import com.avfusion.apps.myanime.utility.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeListUiState>(AnimeListUiState.Loading)
    val uiState: StateFlow<AnimeListUiState> = _uiState.asStateFlow()

    private var currentPage = 1

    init {
        viewModelScope.launch {
            combine(
                repository.getAnimeFlow(),
                networkMonitor.isOnline
            ) { animeList, isOnline ->
                if (animeList.isEmpty()) {
                    if (isOnline) {
                        AnimeListUiState.Loading
                    } else {
                        AnimeListUiState.ErrorNoDataAndNoNetwork
                    }
                } else {
                    AnimeListUiState.Success(animeList)
                }
            }.collect { state ->
                _uiState.value = state

                if (state is AnimeListUiState.Loading) {
                    fetchAnime()
                }
            }
        }
    }

    fun fetchNextPage() {
        currentPage++
        fetchAnime()
    }

    fun fetchAnime() {
        viewModelScope.launch {
            repository.fetchAndSyncAnime(currentPage)
        }
    }
}