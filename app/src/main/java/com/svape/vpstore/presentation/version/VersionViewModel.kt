package com.svape.vpstore.presentation.version

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svape.vpstore.BuildConfig
import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.VersionInfo
import com.svape.vpstore.domain.model.VersionStatus
import com.svape.vpstore.domain.usecase.CheckVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VersionUiState(
    val isLoading: Boolean = false,
    val versionInfo: VersionInfo? = null,
    val error: String? = null,
    val canNavigate: Boolean = false
)

@HiltViewModel
class VersionViewModel @Inject constructor(
    private val checkVersionUseCase: CheckVersionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VersionUiState())
    val uiState: StateFlow<VersionUiState> = _uiState.asStateFlow()

    init { checkVersion() }

    fun checkVersion() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = checkVersionUseCase(BuildConfig.VERSION_NAME)) {
                is Resource.Loading -> Unit
                is Resource.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    versionInfo = result.data,
                    canNavigate = result.data.status == VersionStatus.UP_TO_DATE
                )
                is Resource.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.message
                )
            }
        }
    }

    fun forceContinue() {
        _uiState.value = _uiState.value.copy(canNavigate = true)
    }
}