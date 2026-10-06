package com.arovastudio.crashsquad.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arovastudio.crashsquad.R
import com.arovastudio.crashsquad.components.GameHeader
import com.arovastudio.crashsquad.components.OriginalGameVisuals
import com.arovastudio.crashsquad.data.GameProgressEntity
import com.arovastudio.crashsquad.data.HeroProgressEntity
import com.arovastudio.crashsquad.heroes.HeroRegistry
import com.arovastudio.crashsquad.ui.theme.CoinGold

@Composable
fun HeroesScreen(
    progress: GameProgressEntity?,
    heroProgressList: List<HeroProgressEntity>,
    onBackClick: () -> Unit,
    onUpgradeHero: (String) -> Unit,
    onUnlockHero: (String) -> Unit,
    onSelectHero: (String) -> Unit,
    onShopClick: () -> Unit
) {
    BackHandler { onBackClick() }

    val coins = progress?.coins ?: 1250
    val gems = progress?.gems ?: 50
    val selectedHeroId = progress?.selectedHeroId ?: "blaze"

    val heroes = HeroRegistry.ALL_HEROES
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentHero = heroes[currentIndex]

    val heroData = heroProgressList.find { it.heroId == currentHero.id }
    val heroLevel = heroData?.level ?: 1
    val isUnlocked = (currentHero.id == "blaze") || (heroData?.isUnlocked == true)
    val isSelected = currentHero.id == selectedHeroId

    val currentPower = currentHero.calculatePower(heroLevel)
    val upgradeCost = currentHero.upgradeCost(heroLevel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E7))
            .testTag("screen_heroes")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GameHeader(
                coins = coins,
                gems = gems,
                onBackClick = onBackClick,
                onGemsPlusClick = onShopClick
            )

            Text(
                text = "HERO SQUAD",
                color = Color(0xFF4E342E),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        currentIndex = if (currentIndex > 0) currentIndex - 1 else heroes.lastIndex
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_hero_prev")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Hero",
                        tint = Color(0xFF4E342E),
                        modifier = Modifier.size(40.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFFFFFDE7),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .shadow(12.dp, RoundedCornerShape(28.dp))
                        .border(3.dp, Color(0xFFFFE082), RoundedCornerShape(28.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(175.dp)
                                .shadow(8.dp, RoundedCornerShape(24.dp))
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFFFF9C4))
                                .border(3.dp, Color.White, RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            when (currentHero.id) {
                                "blaze" -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_hero_blaze_full),
                                        contentDescription = currentHero.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                "aqua" -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_hero_aqua_full),
                                        contentDescription = currentHero.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                else -> {
                                    androidx.compose.foundation.Canvas(modifier = Modifier.size(140.dp)) {
                                        OriginalGameVisuals.drawHero(
                                            this,
                                            currentHero,
                                            size.width / 2f,
                                            size.height / 2f,
                                            50f
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentHero.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFBF360C)
                        )

                        Text(
                            text = "Level $heroLevel",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFECB3))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = Color(0xFFF57C00),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Power $currentPower",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = Color(0xFFE65100)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentHero.abilityDescription,
                            fontSize = 12.sp,
                            color = Color(0xFF546E7A),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }
                }

                IconButton(
                    onClick = {
                        currentIndex = if (currentIndex < heroes.lastIndex) currentIndex + 1 else 0
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_hero_next")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Hero",
                        tint = Color(0xFF4E342E),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isUnlocked) {
                    Button(
                        onClick = { onUpgradeHero(currentHero.id) },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(68.dp)
                            .shadow(10.dp, RoundedCornerShape(34.dp))
                            .testTag("btn_upgrade_hero"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                        border = androidx.compose.foundation.BorderStroke(3.dp, Color.White),
                        shape = RoundedCornerShape(34.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Upgrade",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = CoinGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$upgradeCost",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFFF9C4)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (isSelected) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Selected for Slingshot",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Button(
                            onClick = { onSelectHero(currentHero.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.testTag("btn_select_hero")
                        ) {
                            Text("Select Hero", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = { onUnlockHero(currentHero.id) },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(64.dp)
                            .shadow(10.dp, RoundedCornerShape(32.dp))
                            .testTag("btn_unlock_hero"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA)),
                        border = androidx.compose.foundation.BorderStroke(3.dp, Color.White),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Diamond, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Unlock • ${currentHero.unlockGemsCost} Gems",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
