package com.dwp.cameraportrait

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.concurrent.Executors
import com.dwp.cameraportrait.ui.theme.CameraPortraitTheme

class MainActivity : ComponentActivity() {

    private fun captureImage(state: CameraState) {

        // Not bound to a valid Camera
        val imageCapture = state.imageCapture

        val executor = Executors.newSingleThreadExecutor()

        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    // log success
                    Log.d("MainActivity", "Image captured successfully")
                    super.onCaptureSuccess(image)
                    var bitmap = image.toBitmap()
                    val imageProcessor = ImageProcessor()
                    bitmap = imageProcessor.convertTo400By600Pixels(bitmap, 400)
                    //bitmap = imageProcessor.convertToGrayscale(bitmap)
                    bitmap = imageProcessor.rotateImage(bitmap, 90f)
                    imageProcessor.saveImage(bitmap, contentResolver)
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    // log error
                    Log.e("MainActivity", "Error capturing image", exception)
                    super.onError(exception)
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var cameraState by remember { mutableStateOf(CameraState()) }
            CameraPortraitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        PictureFrameBorder(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        {
                            CameraComponent(
                                modifier = Modifier.fillMaxSize(),
                                state = cameraState
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Row {
                                    Button(
                                        onClick = {
                                            // log that we are capturing
                                            Log.d("MainActivity", "Capture button clicked")
                                            // call captureImage()
                                            captureImage(cameraState)
                                        }
                                    ) {
                                        Text(text = "Capture")
                                    }
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
        }
    }


    @Composable
    fun ZoomControls(cameraState: CameraState, onZoomIn: () -> Unit, onZoomOut: () -> Unit) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Zoom Level: ${cameraState.zoomLevel.toString().take(3)}x")
            androidx.compose.foundation.layout.Row {
                Button(onClick = onZoomIn) {
                    Text("Zoom In")
                }
                Button(onClick = onZoomOut) {
                    Text("Zoom Out")
                }
            }
        }
    }
}


