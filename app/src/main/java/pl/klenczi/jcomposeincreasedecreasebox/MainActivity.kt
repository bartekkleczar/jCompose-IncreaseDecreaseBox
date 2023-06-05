package pl.klenczi.jcomposeincreasedecreasebox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var cornerRadiusState by remember { mutableStateOf(60.dp) }
            var sizeState by remember { mutableStateOf(200.dp) }
            val size by animateDpAsState(
                targetValue = sizeState,
                tween(
                    durationMillis = 100
                )
            )
            val infiniteTransition = rememberInfiniteTransition()
            val color by infiniteTransition.animateColor(
                initialValue = Color(0xFF536DFE),
                targetValue = Color(0xFF009688),
                animationSpec = infiniteRepeatable(
                    tween(
                        durationMillis = 2000
                    ),
                    repeatMode = RepeatMode.Reverse

                )
            )
            val interactionSource = remember { MutableInteractionSource() }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .background(color, shape = RoundedCornerShape(cornerRadiusState)),
                    contentAlignment = Alignment.Center
                )
                {
                    Column() {
                        Text(
                            text = "INCREASE",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null

                            ) {
                                sizeState += 10.dp
                                cornerRadiusState += 10.dp
                                Log.i("Main", "corner: $cornerRadiusState, size: $sizeState")
                            }
                        )
                        Text(
                            text = "DECREASE",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null

                            ) {
                                if (sizeState > 150.dp) {
                                    sizeState -= 10.dp
                                }
                                if(cornerRadiusState >= 20.dp){
                                    cornerRadiusState -= 10.dp
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}