package com.dwp.cameraportrait

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.dwp.cameraportrait.ui.theme.CameraPortraitTheme

class MainActivity : ComponentActivity() {
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


