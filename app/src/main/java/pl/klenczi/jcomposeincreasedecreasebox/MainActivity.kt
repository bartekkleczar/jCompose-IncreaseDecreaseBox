package pl.klenczi.jcomposeincreasedecreasebox

import android.content.Context
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.preference.PreferenceManager


class MyPreferences(context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sizeChangeValue = 10.dp
            val cornerChangeValue = (7.25).dp
            val sizeBorderValue = 150.dp

            val context = LocalContext.current
            val preferences = remember { MyPreferences(context) }
            var clickCount by rememberSaveable { mutableIntStateOf(preferences.getInt("clickCount", 0)) }


            val constraints = ConstraintSet {
                val countText = createRefFor("countText")
                val countTextVertically = createGuidelineFromTop(0.01f)

                constrain(countText) {
                    top.linkTo(countTextVertically)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            }

            var cornerRadiusState by remember { mutableStateOf(0.dp) }
            var sizeState by remember { mutableStateOf(150.dp) }
            val size by animateDpAsState(
                targetValue = sizeState,
                tween(
                    durationMillis = 350
                )
            )
            val infiniteTransition = rememberInfiniteTransition()
            val color by infiniteTransition.animateColor(
                initialValue = Color.Red,
                targetValue = Color.Green,
                animationSpec = infiniteRepeatable(
                    tween(
                        durationMillis = 10_000
                    ),
                    repeatMode = RepeatMode.Reverse

                )
            )
            val interactionSource = remember { MutableInteractionSource() }
            ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "${preferences.getInt("clickCount", clickCount)}",
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.layoutId("countText")
                )

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
                        Column(
                            modifier = Modifier
                                .width(sizeBorderValue)
                                .height(sizeBorderValue),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "INCREASE",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        clickCount += 1
                                        preferences.saveInt("clickCount", clickCount)
                                        sizeState += sizeChangeValue
                                        cornerRadiusState += cornerChangeValue
                                        //Log.i("Main", "corner: $cornerRadiusState, size: $sizeState")
                                    }
                            )
                            Text(
                                text = "DECREASE",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        clickCount += 1
                                        preferences.saveInt("clickCount", clickCount)
                                        if (cornerRadiusState >= (cornerChangeValue * 1) && sizeState > sizeBorderValue) {
                                            cornerRadiusState -= cornerChangeValue
                                            sizeState -= sizeChangeValue
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}