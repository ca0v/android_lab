package com.dwp.cameraportrait

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun BitmapComponent(bitmap: Bitmap?) {
    var isExpanded by remember { mutableStateOf(false) }
    val targetSize by animateFloatAsState(
        targetValue = if (isExpanded) 1000f else 50f,
        animationSpec = tween(durationMillis = 300),
        label = "sizeAnimation"
    )

    // Handle initial expansion when bitmap first appears
    LaunchedEffect(bitmap) {
        if (bitmap != null && targetSize == 50f) {
            isExpanded = true
            delay(1000)
            isExpanded = false
        }
    }

    bitmap?.let { bmp ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = if (isExpanded) Alignment.Center else Alignment.TopEnd
        ) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Camera capture",
                modifier = Modifier
                    .then(
                        if (isExpanded) Modifier.fillMaxSize()
                        else Modifier
                            .size(targetSize.dp)
                            .padding(top = 16.dp, end = 16.dp)
                    )
                    .graphicsLayer(rotationZ = 90f)
                    .clickable { isExpanded = !isExpanded }
            )
        }
    }
}