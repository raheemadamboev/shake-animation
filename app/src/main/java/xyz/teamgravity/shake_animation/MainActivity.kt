package xyz.teamgravity.shake_animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import xyz.teamgravity.shake_animation.ui.theme.Green
import xyz.teamgravity.shake_animation.ui.theme.Red
import xyz.teamgravity.shake_animation.ui.theme.ShakeanimationTheme
import xyz.teamgravity.shake_animation.ui.theme.White

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShakeanimationTheme(
                darkTheme = true
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var password: String by rememberSaveable { mutableStateOf("") }
                    var state: LoginState by rememberSaveable { mutableStateOf(LoginState.Input) }
                    val color: Color by animateColorAsState(
                        targetValue = when (state) {
                            LoginState.Input -> White
                            LoginState.Wrong -> Red
                            LoginState.Correct -> Green
                        },
                        label = "Button color"
                    )

                    val controller = rememberShakeController()

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TextField(
                            value = password,
                            onValueChange = { value ->
                                password = value
                                state = LoginState.Input
                            },
                            label = {
                                Text(text = stringResource(id = R.string.password))
                            },
                            isError = state == LoginState.Wrong,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(8.dp)
                                .shake(controller)
                                .border(
                                    width = 2.dp,
                                    color = color,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .background(
                                    color = color.copy(
                                        alpha = 0.1F
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        state = if (password == "raheem") LoginState.Correct else LoginState.Wrong
                                        when (state) {
                                            LoginState.Input -> Unit
                                            LoginState.Wrong -> controller.shake(
                                                ShakeConfig(
                                                    iterations = 4,
                                                    intensity = 2_000F,
                                                    rotateY = 15F,
                                                    translateX = 40F
                                                )
                                            )

                                            LoginState.Correct -> controller.shake(
                                                ShakeConfig(
                                                    iterations = 4,
                                                    intensity = 1_000F,
                                                    rotateX = -20F,
                                                    translateY = 20F
                                                )
                                            )
                                        }
                                    }
                                }
                                .clip(RoundedCornerShape(5.dp))
                                .padding(
                                    horizontal = 24.dp,
                                    vertical = 8.dp
                                ),
                        ) {
                            AnimatedContent(
                                targetState = state,
                                transitionSpec = {
                                    slideInVertically(
                                        animationSpec = spring(
                                            stiffness = Spring.StiffnessMedium
                                        ),
                                        initialOffsetY = { -it }
                                    ) + fadeIn() togetherWith
                                            slideOutVertically(
                                                animationSpec = spring(
                                                    stiffness = Spring.StiffnessHigh
                                                ),
                                                targetOffsetY = { it }
                                            ) + fadeOut() using SizeTransform(
                                        clip = false
                                    )
                                },
                                contentAlignment = Alignment.Center,
                                label = "Button text"
                            ) { state ->
                                Text(
                                    text = stringResource(
                                        id = when (state) {
                                            LoginState.Input -> R.string.login
                                            LoginState.Wrong -> R.string.try_again
                                            LoginState.Correct -> R.string.success
                                        }
                                    ),
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class LoginState {
    Input,
    Wrong,
    Correct
}