package com.hope.tubitak_bitki.presentation.camera

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnPhotoTaken -> {
                _state.update { it.copy(capturedBitmap = event.bitmap, capturedVideoUri = null) }
            }
            is CameraEvent.OnVideoTaken -> {
                _state.update { it.copy(capturedVideoUri = event.uri, capturedBitmap = null, isRecording = false) }
            }
            is CameraEvent.OnPermissionResult -> {
                _state.update { it.copy(hasPermission = event.isGranted) }
            }
            is CameraEvent.OnModeChange -> {
                _state.update { it.copy(isVideoMode = event.isVideo) }
            }
            CameraEvent.OnRecordStart -> {
                _state.update { it.copy(isRecording = true) }
            }
            CameraEvent.OnRecordStop -> {
                _state.update { it.copy(isRecording = false) }
            }
            CameraEvent.OnRetake -> {
                _state.update { it.copy(capturedBitmap = null, capturedVideoUri = null, isRecording = false) }
            }
        }
    }
}