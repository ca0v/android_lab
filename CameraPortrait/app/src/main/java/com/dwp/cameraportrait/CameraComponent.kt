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
import androidx.camera.core.ZoomState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

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

            LaunchedEffect(key1 = previewView) { // Initialize camera
                try {
                    if (previewView == null) {
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
                            state.camera?.cameraControl!!.setZoomRatio(state.camera!!.cameraInfo.zoomState.value!!.minZoomRatio)
                            isCameraInitializing = false // Camera is now initialized
                        }, ContextCompat.getMainExecutor(context))
                    }
                } catch (e: Exception) {
                    Log.e("CameraComponent", "Error initializing camera", e)
                    isCameraInitializing = false // Stop loading in case of error
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
                        Log.d("CameraComponent", "Calculated zoom ratio: $calculatedZoomRatio, which is between $minZoomRatio and $maxZoomRatio")

                        cam.cameraControl.setZoomRatio(calculatedZoomRatio)
                        state.zoomRatio = calculatedZoomRatio
                    }
                }
            }

            if (isCameraInitializing) {
                Text("Loading Camera...")
            } else {
                previewView?.let { pv ->
                    AndroidView({ pv })
                } ?: Text("Camera Initialization Failed")
            }
        } else {
            Button(onClick = { launcher.launch(cameraPermission) }) {
                Text("Request Camera Permission")
            }
        }
    }
}