package com.hope.tubitak_bitki.presentation.camera

import android.graphics.Bitmap
import android.net.Uri

data class CameraState(
    val capturedBitmap: Bitmap? = null,
    val capturedVideoUri: Uri? = null,
    val isRecording: Boolean = false,
    val isVideoMode: Boolean = false,
    val hasPermission: Boolean = false
)

sealed class CameraEvent {
    data class OnPhotoTaken(val bitmap: Bitmap) : CameraEvent()
    data class OnVideoTaken(val uri: Uri) : CameraEvent()
    data class OnPermissionResult(val isGranted: Boolean) : CameraEvent()
    object OnRecordStart : CameraEvent()
    object OnRecordStop : CameraEvent()
    data class OnModeChange(val isVideo: Boolean) : CameraEvent()
    object OnRetake : CameraEvent()
}