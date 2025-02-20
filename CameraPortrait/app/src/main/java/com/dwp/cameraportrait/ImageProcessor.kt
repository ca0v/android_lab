package com.dwp.cameraportrait

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.os.VibrationEffect
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import java.util.concurrent.Executors

class ImageProcessor {

    fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.postRotate(degrees)
        return android.graphics.Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                // Get pixel color
                val pixelColor = bitmap.getPixel(x, y)
                // Extract color components
                val red = Color.red(pixelColor)
                val green = Color.green(pixelColor)
                val blue = Color.blue(pixelColor)
                // Calculate grayscale value using weighted sum
                val gray = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
                // Set pixel in grayscale bitmap
                grayscaleBitmap.setPixel(x, y, Color.rgb(gray, gray, gray))
            }
        }
        return grayscaleBitmap
    }

    fun resample(bitmap: Bitmap, width: Int = 400): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val height = (width / aspectRatio).toInt()
        return if (bitmap.width == width && bitmap.height == height) {
            bitmap
        } else {
            Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
    }

    fun saveImage(bitmap: Bitmap?, contentResolver: ContentResolver) {
        if (bitmap == null) return
        val filename = "IMG_${UUID.randomUUID()}.jpg"
        val fos: FileOutputStream?

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                fos = contentResolver.openOutputStream(imageUri) as? FileOutputStream
            } else {
                fos = null
            }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    }

    fun captureImage(state: CameraState, onImageCaptured: (Bitmap?) -> Unit) {

        // Not bound to a valid Camera
        val imageCapture = state.imageCapture

        val executor = Executors.newSingleThreadExecutor()

        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    // log success
                    Log.d("MainActivity", "Image captured successfully")
                    try {
                        var bitmap = image.toBitmap()
                        onImageCaptured(bitmap)
                        bitmap = resample(bitmap, 400)
                        bitmap = convertToGrayscale(bitmap)
                        bitmap = rotateImage(bitmap, 90f)
                        saveImage(bitmap, state.contentResolver)
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error processing image", e)
                    } finally {
                        image.close()
                    }
                }


                override fun onError(exception: ImageCaptureException) {
                    // log error
                    Log.e("MainActivity", "Error capturing image", exception)
                    super.onError(exception)
                }
            }
        )
    }


}