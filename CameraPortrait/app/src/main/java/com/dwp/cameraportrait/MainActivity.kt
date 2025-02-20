package com.dwp.cameraportrait

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import com.dwp.cameraportrait.ui.theme.CameraPortraitTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val cameraState by remember { mutableStateOf(CameraState()) }
            cameraState.contentResolver = contentResolver
            CameraPortraitTheme {
                MyScreenComponent(cameraState)
                BitmapComponent(cameraState.bitmap)
            }
        }
    }
}

@Composable
fun BitmapComponent(bitmap: Bitmap?) {
    // Observe the bitmap reactively

    // Render the bitmap if it exists
    bitmap?.let { bmp ->
        Image(
            bitmap = bmp.asImageBitmap(),
            contentDescription = "Camera capture",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Maintain aspect ratio, adjust as needed
        )
    } ?: run {
        // Optional: Show placeholder when bitmap is null
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("No image captured")
        }
    }
}