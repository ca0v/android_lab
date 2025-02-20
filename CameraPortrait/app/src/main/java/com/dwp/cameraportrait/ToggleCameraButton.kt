package com.dwp.cameraportrait

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ToggleCameraButton(cameraState: CameraState) {
    val scope =
        rememberCoroutineScope()  // Creates a coroutine scope tied to the composable's lifecycle

    Button(onClick = {
        scope.launch {  // Launches a coroutine in the scope
            cameraState.updateSnapshotTime()
            if (cameraState.isCameraOn) {
                cameraState.isCameraOn = false
                delay(50L)  // Wait 50ms
                cameraState.toggleCamera()
                delay(50L)  // Wait another 50ms
                cameraState.isCameraOn = true
            } else {
                cameraState.toggleCamera()
                cameraState.isCameraOn = true
            }
        }
    }) {
        Text("Toggle Camera")
    }
}