package com.dwp.cameraportrait

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue

class CameraState(zoomLevel: Float = 0.5f) {
    var zoomLevel by mutableFloatStateOf(zoomLevel)
        private set
    fun zoomIn() {
        zoomLevel = (zoomLevel + 0.1f).coerceAtMost(1.0f)
    }

    fun zoomOut() {
        zoomLevel = (zoomLevel - 0.1f).coerceAtLeast(0.1f)
    }
}