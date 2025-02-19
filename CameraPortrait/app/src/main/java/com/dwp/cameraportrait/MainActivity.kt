package com.dwp.cameraportrait

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.dwp.cameraportrait.ui.theme.CameraPortraitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CameraPortraitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PictureFrameBorder(
                        modifier = Modifier.padding(innerPadding)
                    )
                    {
                        CameraComponent(
                            Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PictureFrameBorder(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    // create a container with a border with a 10dp stroke and a red color
    Box(
        modifier = modifier
            .border(width = 10.dp, color = Color.Red)
            .padding(16.dp),
    ) {
        content()
    }
}

@Composable
fun CameraComponent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA

    // State to hold the permission status
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Launcher for requesting permission
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
    }

    // Function to check if permission is granted
    fun checkCameraPermission() {
        hasCameraPermission = PermissionChecker.checkSelfPermission(
            context,
            cameraPermission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    // Check permission on initial composition
    checkCameraPermission()

    Column(modifier = modifier) {
        if (hasCameraPermission) {
            Text("Camera permission granted")
            // Here you can add your camera functionality
        } else {
            Button(onClick = { launcher.launch(cameraPermission) }) {
                Text("Request Camera Permission")
            }
        }
    }
}
