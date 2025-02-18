package com.dwp.numbermemory

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dwp.numbermemory.components.DigitTileComponent
import com.dwp.numbermemory.components.HorizontalLayoutOfDigitTiles
import com.dwp.numbermemory.components.MyScreen
import com.dwp.numbermemory.components.NumberMemoryViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val model = NumberMemoryViewModel()
        val randomDigits = RandomNumberGeneratorHelper().getRandomString(6)
        model.updateDigits(randomDigits.map { it.toString().toInt() });

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }

        setContentView(
            ComposeView(this).apply {
                consumeWindowInsets = false
                // bring "TEST" in the middle of the screen
                setContent {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyScreen(model = model)
                    }

                    }
            }
        )
    }
}