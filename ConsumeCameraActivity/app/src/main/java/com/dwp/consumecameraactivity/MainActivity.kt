package com.dwp.consumecameraactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.dwp.consumecameraactivity.ui.theme.ConsumeCameraActivityTheme

class MainActivity : ComponentActivity() {
    private var capturedBitmap by mutableStateOf<Bitmap?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                capturedBitmap = result.data?.extras?.getParcelable("data") as? Bitmap
            }
        }

        enableEdgeToEdge()
        setContent {
            ConsumeCameraActivityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CameraScreen(
                        bitmap = capturedBitmap,
                        onTakePhoto = {
                            launchCamera { cameraIntent ->
                                takePictureLauncher.launch(cameraIntent)
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun launchCamera(onPermissionGranted: (Intent) -> Unit) {
        val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (isCameraPermissionGranted) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            onPermissionGranted(cameraIntent)
            return
        }

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                onPermissionGranted(cameraIntent)
            }
        }.launch(Manifest.permission.CAMERA)
    }
}