package com.arovastudio.crashsquad.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.arovastudio.crashsquad.components.GameHeader
import com.arovastudio.crashsquad.data.GameProgressEntity
import com.arovastudio.crashsquad.heroes.HeroRegistry

@Composable
fun HomeScreen(
    progress: GameProgressEntity?,
    onPlayClick: () -> Unit,
    onHeroesClick: () -> Unit,
    onShopClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDailyRewardClick: () -> Unit,
    onMissionsClick: () -> Unit
) {
    val coins = progress?.coins ?: 1250
    val gems = progress?.gems ?: 50
    val currentLevel = progress?.currentLevel ?: 1
    val selectedHeroId = progress?.selectedHeroId ?: "blaze"
    val hero = HeroRegistry.getHeroById(selectedHeroId)

    val infiniteTransition = rememberInfiniteTransition(label = "home_anim")
    val heroFloat by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_float"
    )
    val buttonPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("screen_home")
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_tropical_island_bg),
            contentDescription = "Tropical Island",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x33000000),
                            Color(0x00000000),
                            Color(0x66000000)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameHeader(
                coins = coins,
                gems = gems,
                onSettingsClick = onSettingsClick,
                onGemsPlusClick = onShopClick
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xE6263238), Color(0xCC37474F))
                            )
                        )
                        .border(1.5.dp, Color(0x88FFD54F), RoundedCornerShape(20.dp))
                        .clickable { onMissionsClick() }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .testTag("btn_missions")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = "Missions",
                            tint = Color(0xFFFFCA28),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Missions",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xE64A148C), Color(0xCC7B1FA2))
                            )
                        )
                        .border(1.5.dp, Color(0x88E1BEE7), RoundedCornerShape(20.dp))
                        .clickable { onDailyRewardClick() }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                        .testTag("btn_daily_reward")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Daily Reward",
                            tint = Color(0xFFE1BEE7),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Daily Gift",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .size(240.dp)
                    .offset(y = heroFloat.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(210.dp)
                        .shadow(16.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF))
                )

                val heroImageRes = when (hero.id) {
                    "aqua" -> R.drawable.img_hero_aqua_full
                    else -> R.drawable.img_hero_blaze_full
                }
                Image(
                    painter = painterResource(id = heroImageRes),
                    contentDescription = hero.name,
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                        .border(3.5.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .height(76.dp)
                        .scale(buttonPulse)
                        .shadow(16.dp, RoundedCornerShape(38.dp))
                        .clip(RoundedCornerShape(38.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFEE58),
                                    Color(0xFFFFB300),
                                    Color(0xFFFF6F00)
                                )
                            )
                        )
                        .border(3.5.dp, Color.White, RoundedCornerShape(38.dp))
                        .clickable { onPlayClick() }
                        .testTag("btn_play_home"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Play",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color(0xFFBF360C),
                                        offset = Offset(2f, 3f),
                                        blurRadius = 4f
                                    )
                                )
                            )
                            Text(
                                text = "Level $currentLevel",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFFF9C4)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.88f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .shadow(10.dp, RoundedCornerShape(22.dp))
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF4FC3F7), Color(0xFF0288D1), Color(0xFF01579B))
                                )
                            )
                            .border(2.5.dp, Color.White, RoundedCornerShape(22.dp))
                            .clickable { onHeroesClick() }
                            .testTag("btn_nav_heroes"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Heroes",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Heroes",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .shadow(10.dp, RoundedCornerShape(22.dp))
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFCE93D8), Color(0xFFAB47BC), Color(0xFF6A1B9A))
                                )
                            )
                            .border(2.5.dp, Color.White, RoundedCornerShape(22.dp))
                            .clickable { onShopClick() }
                            .testTag("btn_nav_shop"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Shop",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Shop",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
