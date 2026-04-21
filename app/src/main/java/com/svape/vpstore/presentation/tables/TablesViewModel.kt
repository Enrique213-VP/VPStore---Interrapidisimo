package com.svape.vpstore.presentation.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.DataTable
import com.svape.vpstore.domain.repository.AuthRepository
import com.svape.vpstore.domain.usecase.GetTablesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TablesUiState(
    val tables: List<DataTable> = emptyList(),
    val isSyncing: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class TablesViewModel @Inject constructor(
    private val getTablesUseCase: GetTablesUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TablesUiState())
    val uiState: StateFlow<TablesUiState> = _uiState.asStateFlow()

    init {
        observeLocalTables()
        sync()
    }

    private fun observeLocalTables() {
        getTablesUseCase.getLocal()
            .onEach { tables -> _uiState.value = _uiState.value.copy(tables = tables) }
            .launchIn(viewModelScope)
    }

    fun sync() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, error = null)
            val token = authRepository.getLocalUser()?.token.orEmpty()
            when (val result = getTablesUseCase.sync(token)) {
                is Resource.Loading -> Unit
                is Resource.Success -> _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    successMessage = "${result.data.size} tables synced"
                )
                is Resource.Error -> _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    error = result.message
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}