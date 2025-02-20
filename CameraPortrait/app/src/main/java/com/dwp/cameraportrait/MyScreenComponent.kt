package com.dwp.cameraportrait

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun MyScreenComponent(cameraState: CameraState) {
    val haptic = LocalHapticFeedback.current
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
                            ImageProcessor().captureImage(cameraState) {
                                bitmap ->
                                Log.d("MainActivity", "Image captured")
                                if (bitmap != null) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    cameraState.bitmap = bitmap
                                } else {
                                    Log.e("MainActivity", "Bitmap is null")
                                }
                            }
                        }
                    ) {
                        Text(text = "Capture")
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