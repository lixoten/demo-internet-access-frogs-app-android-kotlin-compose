package com.example.frogs.ui.screens

import com.example.frogs.model.FrogRec

sealed interface FrogsUiState {
    data class Success(val frogList: List<FrogRec>) : FrogsUiState
    object Error : FrogsUiState
    object Loading : FrogsUiState
}