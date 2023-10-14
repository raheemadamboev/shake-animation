package xyz.teamgravity.shake_animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberShakeController(): ShakeController {
    return remember { ShakeController() }
}