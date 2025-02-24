package com.dwp.cameraportrait

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MyScreenComponent(cameraState: CameraState) {
    val haptic = LocalHapticFeedback.current
    val secondsSinceLastSnapshotState = remember { mutableStateOf(cameraState.getSecondsUntilShutdown()) }

    LaunchedEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                secondsSinceLastSnapshotState.value = cameraState.getSecondsUntilShutdown()
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            PictureFrameBorder(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            {
                CameraComponent(
                    modifier = Modifier.fillMaxWidth(),
                    state = cameraState
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            // log that we are capturing
                            Log.d("MainActivity", "Capture button clicked")
                            cameraState.updateSnapshotTime()
                            if (!cameraState.isCameraOn) {
                                cameraState.isCameraOn = true
                                Log.d("MainActivity", "Camera turned on")
                                return@Button
                            }
                            ImageProcessor().captureImage(cameraState) {
                                bitmap ->
                                Log.d("MainActivity", "Image captured")
                                if (bitmap != null) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    cameraState.bitmap = bitmap
                                    cameraState.updateSnapshotTime()
                                } else {
                                    Log.e("MainActivity", "Bitmap is null")
                                }
                            }
                        }
                    ) {
                        Text(
                            text = if (!cameraState.isCameraOn) {
                                "Power On"
                            } else {
                                "Snapshot"
                            }, textAlign = TextAlign.Center)
                    }
                    ZoomControls(
                        cameraState = cameraState,
                        onZoomIn = { cameraState.zoomIn() },
                        onZoomOut = { cameraState.zoomOut() }
                    )
                }
            }

        }
    }
}