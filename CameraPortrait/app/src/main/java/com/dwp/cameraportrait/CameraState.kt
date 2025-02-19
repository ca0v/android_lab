package com.dwp.cameraportrait

data class CameraState(
    val zoomLevel: Float = 1.0f
) {
    fun zoomIn(): CameraState {
        return copy(zoomLevel = (zoomLevel + 0.1f).coerceAtMost(2.0f))
    }

    fun zoomOut(): CameraState {
        return copy(zoomLevel = (zoomLevel - 0.1f).coerceAtLeast(1.0f))
    }
}