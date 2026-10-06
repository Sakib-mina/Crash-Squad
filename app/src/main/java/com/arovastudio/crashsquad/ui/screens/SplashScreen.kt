package com.arovastudio.crashsquad.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arovastudio.crashsquad.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var progressTarget by remember { mutableFloatStateOf(0f) }
    val progress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 1500, easing = LinearEasing),
        label = "splash_progress"
    )

    LaunchedEffect(Unit) {
        progressTarget = 1f
        delay(1800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("screen_splash"),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_tropical_island_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x55000000), Color(0x22000000), Color(0x88000000))
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFEE58), Color(0xFFFF9800), Color(0xFFE65100))
                        )
                    )
                    .border(4.dp, Color.White, RoundedCornerShape(28.dp))
                    .padding(horizontal = 34.dp, vertical = 18.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "CRASH SQUAD",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xFFBF360C),
                                offset = Offset(3f, 4f),
                                blurRadius = 5f
                            )
                        )
                    )
                    Text(
                        text = "ISLAND STRIKE",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFF9C4),
                        letterSpacing = 3.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(38.dp))

            Box(
                modifier = Modifier
                    .size(190.dp)
                    .shadow(14.dp, CircleShape)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_blaze_full),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(22.dp)
                    .shadow(6.dp, RoundedCornerShape(11.dp))
                    .clip(RoundedCornerShape(11.dp))
                    .background(Color(0x88000000))
                    .border(2.5.dp, Color.White, RoundedCornerShape(11.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(22.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFFFEE58), Color(0xFFFF9800), Color(0xFFFF5722))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Loading Island...",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(1f, 2f),
                        blurRadius = 3f
                    )
                )
            )
        }
    }
}
