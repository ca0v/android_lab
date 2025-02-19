package com.dwp.cameraportrait

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

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

    fun convertTo400By600Pixels(bitmap: Bitmap, width: Int = 400): Bitmap {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                fos = resolver.openOutputStream(imageUri) as? FileOutputStream
            } else {
                fos = null
            }
        } else {
            val imageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imageDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
    }

}