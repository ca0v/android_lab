package com.dwp.consumecameraactivity

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen(
    bitmap: Bitmap?,
    onTakePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp), // Optional: padding around the entire content
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image fills most of the screen
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Takes up available space, pushing button down
            )
        } ?: run {
            // Spacer to push button down when no image is present
            Modifier.weight(1f)
        }

        // Button centered near the bottom
        Button(
            onClick = onTakePhoto,
            modifier = Modifier
                .padding(bottom = 16.dp) // Space above button and below image
                .wrapContentHeight()
        ) {
            Text("Take Photo")
        }
    }
}