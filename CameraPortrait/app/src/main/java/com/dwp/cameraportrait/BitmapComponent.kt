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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val COLLAPSED_SIZE = 50f
private const val EXPANDED_SIZE = 1000f
private const val ANIMATION_DURATION_MS = 300
private const val INITIAL_EXPANSION_DURATION_MS: Long = 1000
private val PADDING_TOP = 32.dp
private val PADDING_END = 16.dp
private const val ROTATION_DEGREES = 90f

/**
 * Displays a bitmap that expands to full screen initially for 1 second,
 * then collapses to a clickable thumbnail in a configurable corner.
 * Clicking toggles between expanded and collapsed states.
 *
 * @param bitmap The bitmap to display, or null to render nothing.
 * @param modifier Modifier for customizing the component's layout.
 * @param collapsedAlignment Where to dock the thumbnail when collapsed.
 */
@Composable
fun BitmapComponent(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    collapsedAlignment: Alignment = Alignment.TopEnd
) {
    var isExpanded by remember { mutableStateOf(false) }
    val bitmapKey by remember { mutableStateOf(bitmap) }
    val targetSize by animateFloatAsState(
        targetValue = if (isExpanded) EXPANDED_SIZE else COLLAPSED_SIZE,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
        label = "bitmapSizeAnimation"
    )

    LaunchedEffect(bitmapKey) {
        if (bitmap != null && targetSize == COLLAPSED_SIZE) {
            isExpanded = true
            delay(INITIAL_EXPANSION_DURATION_MS)
            isExpanded = false
        }
    }

    if (bitmap != null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = if (isExpanded) Alignment.Center else collapsedAlignment
        ) {
            Card(
                modifier = Modifier
                    .then(
                        if (isExpanded) Modifier.fillMaxSize()
                        else Modifier
                            .size(targetSize.dp)
                            .padding(top = PADDING_TOP, end = PADDING_END)
                    ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isExpanded) 0.dp else 4.dp
                )
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Camera capture",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(rotationZ = ROTATION_DEGREES)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClickLabel = if (isExpanded) "Collapse image" else "Expand image"
                        ) { isExpanded = !isExpanded }
                        .semantics { role = Role.Button }
                )
            }
        }
    }
}