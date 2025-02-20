package com.dwp.cameraportrait

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dwp.cameraportrait.ui.theme.CameraPortraitTheme

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
                }
                BitmapComponent(cameraState.bitmap)
            }
            ToggleCameraButton(cameraState)
        }
    }
}

