package com.svape.vpstore.presentation.localities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.Locality
import com.svape.vpstore.domain.usecase.GetLocalitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocalitiesUiState(
    val isLoading: Boolean = false,
    val localities: List<Locality> = emptyList(),
    val filtered: List<Locality> = emptyList(),
    val query: String = "",
    val error: String? = null
)

@HiltViewModel
class LocalitiesViewModel @Inject constructor(
    private val getLocalitiesUseCase: GetLocalitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocalitiesUiState())
    val uiState: StateFlow<LocalitiesUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = getLocalitiesUseCase()) {
                is Resource.Loading -> Unit
                is Resource.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    localities = result.data,
                    filtered = result.data
                )
                is Resource.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    fun search(query: String) {
        val filtered = if (query.isBlank()) _uiState.value.localities
        else _uiState.value.localities.filter {
            it.fullName.contains(query, ignoreCase = true) ||
                    it.cityCode.contains(query, ignoreCase = true) ||
                    it.department?.contains(query, ignoreCase = true) == true
        }
        _uiState.value = _uiState.value.copy(query = query, filtered = filtered)
    }
}