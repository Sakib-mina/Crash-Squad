package com.arovastudio.crashsquad.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arovastudio.crashsquad.ui.theme.CoinGold
import com.arovastudio.crashsquad.ui.theme.GemPurple

@Composable
fun LevelResultDialog(
    isVictory: Boolean,
    levelNumber: Int,
    stars: Int,
    score: Int,
    coinsReward: Int,
    gemsReward: Int,
    onNextLevel: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit
) {
    val starScale1 = remember { Animatable(0f) }
    val starScale2 = remember { Animatable(0f) }
    val starScale3 = remember { Animatable(0f) }

    LaunchedEffect(isVictory, stars) {
        if (isVictory) {
            if (stars >= 1) {
                starScale1.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            }
            if (stars >= 2) {
                starScale2.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            }
            if (stars >= 3) {
                starScale3.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            }
        }
    }

    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFFFFFDE7),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .shadow(28.dp, RoundedCornerShape(32.dp))
                .border(
                    width = 4.dp,
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFFFD54F), Color(0xFFFF9800), Color(0xFFE65100))
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .testTag("dialog_level_result")
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isVictory) "Level $levelNumber Complete!" else "Level Failed!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isVictory) Color(0xFF2E7D32) else Color(0xFFC62828),
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color(0x33000000),
                            offset = Offset(1f, 2f),
                            blurRadius = 3f
                        )
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.scale(if (isVictory && stars >= 1) starScale1.value else 0.85f)) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (isVictory && stars >= 1) Color(0xFFFFC107) else Color(0xFFCFD8DC),
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(if (isVictory && stars >= 1) 6.dp else 0.dp, CircleShape)
                        )
                    }
                    Box(modifier = Modifier.scale(if (isVictory && stars >= 2) starScale2.value else 0.95f)) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (isVictory && stars >= 2) Color(0xFFFFB300) else Color(0xFFB0BEC5),
                            modifier = Modifier
                                .size(74.dp)
                                .shadow(if (isVictory && stars >= 2) 8.dp else 0.dp, CircleShape)
                        )
                    }
                    Box(modifier = Modifier.scale(if (isVictory && stars >= 3) starScale3.value else 0.85f)) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (isVictory && stars >= 3) Color(0xFFFFC107) else Color(0xFFCFD8DC),
                            modifier = Modifier
                                .size(56.dp)
                                .shadow(if (isVictory && stars >= 3) 6.dp else 0.dp, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isVictory) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.92f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(6.dp, RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .border(2.dp, Color(0xFFFFE082), RoundedCornerShape(20.dp))
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .shadow(4.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFF9C4)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = CoinGold,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "+$coinsReward",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = Color(0xFF4E342E)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(6.dp, RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .border(2.dp, Color(0xFFE1BEE7), RoundedCornerShape(20.dp))
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .shadow(4.dp, CircleShape)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF3E5F5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Diamond,
                                        contentDescription = null,
                                        tint = GemPurple,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "+$gemsReward",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = Color(0xFF4A148C)
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Out of shots! Refine your aim and blast those fortresses.",
                        color = Color(0xFF5D4037),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (isVictory) onNextLevel() else onRetry()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(64.dp)
                        .shadow(10.dp, RoundedCornerShape(32.dp))
                        .testTag("btn_next_level"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVictory) Color(0xFF43A047) else Color(0xFFFF9800)
                    ),
                    border = androidx.compose.foundation.BorderStroke(2.5.dp, Color.White),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text(
                        text = if (isVictory) "Next Level" else "Try Again",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF29B6F6), Color(0xFF0288D1), Color(0xFF01579B))
                                )
                            )
                            .border(2.5.dp, Color.White, CircleShape)
                            .clickable { onHome() }
                            .testTag("btn_result_home"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFCE93D8), Color(0xFFAB47BC), Color(0xFF6A1B9A))
                                )
                            )
                            .border(2.5.dp, Color.White, CircleShape)
                            .clickable { onRetry() }
                            .testTag("btn_result_retry"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retry",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}
