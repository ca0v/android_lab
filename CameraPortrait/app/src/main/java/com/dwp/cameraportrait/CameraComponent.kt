package com.dwp.cameraportrait

import android.Manifest
import android.util.Log
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.delay

private const val INACTIVITY_TIMEOUT_MS = 60_000L // 1 minute in milliseconds

@Composable
fun CameraComponent(modifier: Modifier = Modifier, state: CameraState) {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    var hasCameraPermission by remember { mutableStateOf(false) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var isCameraInitializing by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
    }

    fun checkCameraPermission() {
        hasCameraPermission = PermissionChecker.checkSelfPermission(
            context,
            cameraPermission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    checkCameraPermission()

    Column(modifier = modifier) {
        if (hasCameraPermission) {
            val activity = context as? ComponentActivity ?: return@Column

            // Manage camera binding/unbinding based on isCameraOn
            LaunchedEffect(state.isCameraOn, previewView) {
                if (state.isCameraOn && previewView == null) {
                    try {
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val currentPreviewView = PreviewView(context)
                            previewView = currentPreviewView
                            val preview = Preview.Builder()
                                .build()
                                .also {
                                    it.surfaceProvider = currentPreviewView.surfaceProvider
                                }
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            state.imageCapture = ImageCapture.Builder()
                                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                                .setTargetRotation(Surface.ROTATION_0)
                                .build()
                            state.camera = cameraProvider.bindToLifecycle(
                                activity,
                                cameraSelector,
                                preview,
                                state.imageCapture
                            )
                            state.camera?.cameraControl!!.setZoomRatio(
                                state.camera!!.cameraInfo.zoomState.value!!.minZoomRatio
                            )
                            isCameraInitializing = false
                        }, ContextCompat.getMainExecutor(context))
                    } catch (e: Exception) {
                        Log.e("CameraComponent", "Error initializing camera", e)
                        isCameraInitializing = false
                    }
                } else if (!state.isCameraOn && previewView != null) {
                    ProcessCameraProvider.getInstance(activity).get().unbindAll()
                    previewView = null
                    state.camera = null
                    isCameraInitializing = true
                }
            }

            // Monitor inactivity and turn off camera if no recent snapshot
            LaunchedEffect(state.isCameraOn, state.lastSnapshotTime) {
                if (state.isCameraOn) {
                    while (true) {
                        val timeSinceLastSnapshot = System.currentTimeMillis() - state.lastSnapshotTime
                        if (timeSinceLastSnapshot >= INACTIVITY_TIMEOUT_MS) {
                            state.isCameraOn = false
                            Log.d("CameraComponent", "Camera turned off due to inactivity")
                            break
                        }
                        delay(1000L) // Check every second
                    }
                }
            }

            // Update zoom when zoomLevel changes
            LaunchedEffect(state.zoomLevel) {
                state.camera?.let { cam ->
                    val zoomState = cam.cameraInfo.zoomState.value
                    zoomState?.let {
                        val maxZoomRatio = it.maxZoomRatio
                        val minZoomRatio = it.minZoomRatio
                        val calculatedZoomRatio = (minZoomRatio + (maxZoomRatio - minZoomRatio) * state.zoomLevel)
                        Log.d(
                            "CameraComponent",
                            "Calculated zoom ratio: $calculatedZoomRatio, between $minZoomRatio and $maxZoomRatio"
                        )
                        cam.cameraControl.setZoomRatio(calculatedZoomRatio)
                        state.zoomRatio = calculatedZoomRatio
                    }
                }
            }

            if (state.isCameraOn) {
                if (isCameraInitializing) {
                    Text("Loading Camera...")
                } else {
                    previewView?.let { pv ->
                        AndroidView(
                            factory = { pv },
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        if (!state.isCameraOn) {
                                            state.isCameraOn = true
                                            Log.d("CameraComponent", "Camera turned on by tap")
                                        }
                                    }
                                }
                        )
                    } ?: Text("Camera Initialization Failed")
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures {
                                state.isCameraOn = true
                                Log.d("CameraComponent", "Camera turned on by tap")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Camera is off (tap to turn on)")
                }
            }
        } else {
            Button(onClick = { launcher.launch(cameraPermission) }) {
                Text("Request Camera Permission")
            }
        }
    }
}