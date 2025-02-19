package com.dwp.cameraportrait

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ZoomControls(cameraState: CameraState, onZoomIn: () -> Unit, onZoomOut: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = onZoomIn,
            ) {
                Text(text = "IN")

            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = "x${(cameraState.zoomLevel * 10).roundToInt()}"
            )
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = onZoomOut
            ) {
                Text("OUT")
            }
        }
    }
}