package com.example.travel.view.flight

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CountdownProgressBar(
    modifier: Modifier = Modifier,
    durationMillis: Long = 10 * 1000L,
    onCountdownComplete: () -> Unit = {}
) {
    var animatedTargetProgress by remember { mutableFloatStateOf(1f) }

    var animationCycleKey by remember { mutableStateOf(0) }

    val currentProgress by animateFloatAsState(
        targetValue = animatedTargetProgress,
        animationSpec = tween(
            durationMillis = if (animatedTargetProgress == 0f) durationMillis.toInt() else 0,
            easing = LinearEasing
        ),
        label = "countdownProgressAnimation"
    )

    LaunchedEffect(key1 = animationCycleKey, key2 = durationMillis) {
        while (true) {
            animatedTargetProgress = 1f
            delay(50)
            animatedTargetProgress = 0f
            delay(durationMillis)
            onCountdownComplete()
            animationCycleKey++
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(currentProgress)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}