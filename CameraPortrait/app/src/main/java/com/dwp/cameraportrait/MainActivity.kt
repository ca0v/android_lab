package com.dwp.cameraportrait

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.dwp.cameraportrait.ui.theme.CameraPortraitTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val cameraState by remember { mutableStateOf(CameraState()) }
            cameraState.contentResolver = contentResolver
            CameraPortraitTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    MyScreenComponent(cameraState)
                    BitmapComponent(cameraState.bitmap)
                }
            }
        }
    }
}

@Composable
fun BitmapComponent(bitmap: Bitmap?) {
    val isExpanded = remember { mutableStateOf(false) }
    val sizeAnim = remember { Animatable(initialValue = 50f) } // Initial size of small icon

    // Trigger animation when bitmap changes
    LaunchedEffect(bitmap) {
        if (bitmap != null) {
            isExpanded.value = true
            sizeAnim.snapTo(1000f) // Large value to represent fullscreen
            delay(1000) // Wait 1 second
            isExpanded.value = false
            sizeAnim.animateTo(
                targetValue = 50f, // Small icon size
                animationSpec = tween(durationMillis = 300)
            )
        }
    }

    bitmap?.let { bmp ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = if (isExpanded.value) Alignment.Center
            else Alignment.BottomEnd
        ) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Camera capture",
                modifier = Modifier
                    .then(
                        if (isExpanded.value) Modifier.fillMaxSize()
                        else Modifier.size(sizeAnim.value.dp)
                    )
            )
        }
    } ?: run {
        // Placeholder when no bitmap
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("No image")
        }
    }
}
