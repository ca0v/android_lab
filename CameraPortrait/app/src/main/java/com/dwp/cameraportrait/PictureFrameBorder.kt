package com.dwp.cameraportrait

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PictureFrameBorder(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    // create a container with a border with a 10dp stroke and a red color
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4f / 6f)
            .border(width = 10.dp, color = Color.Red)
            .padding(16.dp),
    ) {
            content()
    }
}