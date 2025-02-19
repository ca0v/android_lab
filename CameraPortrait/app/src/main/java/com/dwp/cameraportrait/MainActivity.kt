package com.dwp.cameraportrait

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import com.dwp.cameraportrait.ui.theme.CameraPortraitTheme

class MainActivity : ComponentActivity() {

    private fun captureImage(state: CameraState): Bitmap? {
        // return a test bitmap, 100x100
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }

    private fun saveImage(bitmap: Bitmap?) {
        if (bitmap == null) return
        val filename = "IMG_${UUID.randomUUID()}.jpg"
        val fos: FileOutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                fos = resolver.openOutputStream(imageUri) as? FileOutputStream
            } else {
                fos = null
            }
        } else {
            val imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imageDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
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
                                state = cameraState,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            Row {
                                Button(onClick = {
                                    val bitmap = captureImage(cameraState)
                                    saveImage(bitmap)
                                }) {
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


