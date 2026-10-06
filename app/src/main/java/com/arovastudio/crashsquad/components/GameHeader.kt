package com.arovastudio.crashsquad.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arovastudio.crashsquad.ui.theme.CoinGold
import com.arovastudio.crashsquad.ui.theme.GemPurple
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GameHeader(
    coins: Int,
    gems: Int,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    onGemsPlusClick: (() -> Unit)? = null
) {
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF29B6F6), Color(0xFF0288D1))
                        )
                    )
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { onBackClick() }
                    .testTag("btn_back"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.width(4.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CurrencyBadge(
                icon = {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = CoinGold,
                        modifier = Modifier.size(22.dp)
                    )
                },
                amountText = formatter.format(coins),
                backgroundColor = Color(0x99000000),
                modifier = Modifier.testTag("badge_coins")
            )

            CurrencyBadge(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = "Gems",
                        tint = GemPurple,
                        modifier = Modifier.size(22.dp)
                    )
                },
                amountText = formatter.format(gems),
                backgroundColor = Color(0x99000000),
                hasPlus = onGemsPlusClick != null,
                onPlusClick = onGemsPlusClick,
                modifier = Modifier.testTag("badge_gems")
            )
        }

        if (onSettingsClick != null) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
                        )
                    )
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { onSettingsClick() }
                    .testTag("btn_settings"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun CurrencyBadge(
    icon: @Composable () -> Unit,
    amountText: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    hasPlus: Boolean = false,
    onPlusClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .shadow(3.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.5.dp, Color(0x66FFFFFF), RoundedCornerShape(20.dp))
            .padding(start = 6.dp, end = if (hasPlus) 2.dp else 12.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = amountText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            if (hasPlus) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .clickable { onPlusClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Gems",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
