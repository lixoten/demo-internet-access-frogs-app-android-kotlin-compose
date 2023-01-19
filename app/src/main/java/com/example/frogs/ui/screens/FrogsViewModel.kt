package com.example.frogs.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.frogs.FrogsApplication
import com.example.frogs.data.FrogsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FrogsViewModel(private val frogsRepository: FrogsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<FrogsUiState>(FrogsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getFrogRecords()
    }

    fun getFrogRecords() {
        viewModelScope.launch {
            _uiState.value = FrogsUiState.Loading
            _uiState.value = try {
                FrogsUiState.Success(frogsRepository.getFrogsRecords())
            } catch (e: IOException) {
                FrogsUiState.Error
            } catch (e: HttpException) {
                FrogsUiState.Error
            }
        }
    }

    /**
     * Factory for [FrogsViewModel] that takes [FrogsRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FrogsApplication)
                val frogsRepository = application.container.frogsRepository
                FrogsViewModel(frogsRepository = frogsRepository)
            }
        }
    }
}