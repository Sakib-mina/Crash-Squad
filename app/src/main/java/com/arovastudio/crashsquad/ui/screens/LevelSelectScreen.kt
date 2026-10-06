package com.arovastudio.crashsquad.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.arovastudio.crashsquad.components.GameHeader
import com.arovastudio.crashsquad.data.GameProgressEntity
import com.arovastudio.crashsquad.data.LevelRecordEntity
import com.arovastudio.crashsquad.game.GameLevels

@Composable
fun LevelSelectScreen(
    progress: GameProgressEntity?,
    levelRecords: List<LevelRecordEntity>,
    onBackClick: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onShopClick: () -> Unit
) {
    BackHandler { onBackClick() }

    val highestUnlocked = progress?.highestUnlockedLevel ?: 1
    val coins = progress?.coins ?: 1250
    val gems = progress?.gems ?: 50

    val recordsMap = levelRecords.associateBy { it.levelId }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("screen_level_select")
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
                        listOf(Color(0x55000000), Color(0x33000000), Color(0x77000000))
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            GameHeader(
                coins = coins,
                gems = gems,
                onBackClick = onBackClick,
                onGemsPlusClick = onShopClick
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0288D1), Color(0xFF00ACC1))
                        )
                    )
                    .border(2.5.dp, Color.White, RoundedCornerShape(20.dp))
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "SELECT LEVEL",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color(0xFF004D40),
                            offset = Offset(2f, 2f),
                            blurRadius = 3f
                        )
                    )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                itemsIndexed(GameLevels.ALL_LEVELS) { index, level ->
                    val levelNumber = level.levelNumber
                    val isUnlocked = levelNumber <= highestUnlocked
                    val record = recordsMap[levelNumber]
                    val stars = record?.stars ?: 0

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = when (index % 3) {
                            0 -> Alignment.CenterStart
                            1 -> Alignment.Center
                            else -> Alignment.CenterEnd
                        }
                    ) {
                        LevelNodeItem(
                            levelNumber = levelNumber,
                            isUnlocked = isUnlocked,
                            isCurrent = levelNumber == highestUnlocked,
                            stars = stars,
                            onClick = {
                                if (isUnlocked) {
                                    onSelectLevel(levelNumber)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LevelNodeItem(
    levelNumber: Int,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    stars: Int,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("level_node_$levelNumber")
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    when {
                        !isUnlocked -> Brush.verticalGradient(
                            listOf(Color(0xFFB0BEC5), Color(0xFF78909C), Color(0xFF546E7A))
                        )
                        isCurrent -> Brush.verticalGradient(
                            listOf(Color(0xFFFFEE58), Color(0xFFFFB300), Color(0xFFFF6F00))
                        )
                        else -> Brush.verticalGradient(
                            listOf(Color(0xFF81C784), Color(0xFF4CAF50), Color(0xFF2E7D32))
                        )
                    }
                )
                .border(
                    width = if (isCurrent) 4.dp else 3.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isUnlocked) {
                Text(
                    text = "$levelNumber",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = if (isCurrent) Color(0xFFBF360C) else Color(0xFF1B5E20),
                            offset = Offset(2f, 3f),
                            blurRadius = 3f
                        )
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color(0xFF37474F),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (isUnlocked) {
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(3) { starIndex ->
                    val earned = starIndex < stars
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (earned) Color(0xFFFFD54F) else Color(0x66FFFFFF),
                        modifier = Modifier
                            .size(18.dp)
                            .shadow(if (earned) 3.dp else 0.dp, CircleShape)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x88000000))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Locked",
                    color = Color(0xDDFFFFFF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
