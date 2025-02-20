package com.dwp.cameraportrait

import android.graphics.Bitmap
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

const val INACTIVITY_TIMEOUT_MS = 10_000L // 1 minute in milliseconds

class CameraState(zoomLevel: Float = 0f, isCameraOn: Boolean = true) {
    lateinit var imageCapture: ImageCapture
    lateinit var contentResolver: android.content.ContentResolver

    var isCameraOn by mutableStateOf(isCameraOn)

    var bitmap by mutableStateOf<Bitmap?>(null)

    var camera by mutableStateOf<Camera?>(null)

    var zoomRatio by mutableFloatStateOf(zoomLevel)

    var zoomLevel by mutableFloatStateOf(zoomLevel)
        private set

    // Timestamp of the last snapshot in milliseconds since epoch
    var lastSnapshotTime by mutableLongStateOf(System.currentTimeMillis())
        private set

    fun zoomIn() {
        zoomLevel = (zoomLevel + 0.1f).coerceAtMost(1.0f)
        updateSnapshotTime()
    }

    fun zoomOut() {
        zoomLevel = (zoomLevel - 0.1f).coerceAtLeast(0.0f)
        updateSnapshotTime()
    }

    // Call this when a snapshot is taken
    fun updateSnapshotTime() {
        lastSnapshotTime = System.currentTimeMillis()
    }

    fun getSecondsUntilShutdown(): Long {
        if (lastSnapshotTime == 0L) return 0L
        val timeUntilShutdown = INACTIVITY_TIMEOUT_MS - (System.currentTimeMillis() - lastSnapshotTime)
        return (timeUntilShutdown / 1000L)
    }
}