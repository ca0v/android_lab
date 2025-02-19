package com.dwp.cameraportrait

import androidx.camera.core.ImageCapture
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

class CameraState(zoomLevel: Float = 0f) {
    lateinit var imageCapture: ImageCapture
    var camera by mutableStateOf<androidx.camera.core.Camera?>(null)

    var zoomLevel by mutableFloatStateOf(zoomLevel)
        private set
    fun zoomIn() {
        zoomLevel = (zoomLevel + 0.1f).coerceAtMost(1.0f)
    }

    fun zoomOut() {
        zoomLevel = (zoomLevel - 0.1f).coerceAtLeast(0.0f)
    }
}
