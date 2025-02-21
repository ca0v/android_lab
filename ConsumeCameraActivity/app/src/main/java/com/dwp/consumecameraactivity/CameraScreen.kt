package com.dwp.consumecameraactivity

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the image if it exists
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured photo",
                modifier = Modifier
                    .padding(bottom = 16.dp) // Space between image and button
            )
        }

        // Button is always visible
        Button(
            onClick = onTakePhoto,
            modifier = Modifier.padding(top = 16.dp) // Space above button if no image
        ) {
            Text("Take Photo")
        }
    }
}