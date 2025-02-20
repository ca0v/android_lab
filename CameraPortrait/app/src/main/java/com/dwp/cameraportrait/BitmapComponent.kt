package com.dwp.cameraportrait

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

private const val COLLAPSED_SIZE = 50f
private const val EXPANDED_SIZE = 1000f
private const val ANIMATION_DURATION_MS = 300
private const val INITIAL_EXPANSION_DURATION_MS = 1000
private val PADDING_TOP = 32.dp
private val PADDING_END = 16.dp
private const val ROTATION_DEGREES = 90f

@Composable
fun BitmapComponent(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val targetSize by animateFloatAsState(
        targetValue = if (isExpanded) EXPANDED_SIZE else COLLAPSED_SIZE,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
        label = "bitmapSizeAnimation"
    )

    // Handle initial expansion when bitmap first appears
    LaunchedEffect(bitmap) {
        if (bitmap != null && targetSize == COLLAPSED_SIZE) {
            isExpanded = true
            delay(INITIAL_EXPANSION_DURATION_MS.toLong())
            isExpanded = false
        }
    }

    if (bitmap != null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = if (isExpanded) Alignment.Center else Alignment.TopEnd
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Camera capture",
                modifier = Modifier
                    .then(
                        if (isExpanded) Modifier.fillMaxSize()
                        else Modifier
                            .size(targetSize.dp)
                            .padding(top = PADDING_TOP, end = PADDING_END)
                    )
                    .graphicsLayer(rotationZ = ROTATION_DEGREES)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // No ripple effect, customize if needed
                    ) { isExpanded = !isExpanded }
            )
        }
    }
}