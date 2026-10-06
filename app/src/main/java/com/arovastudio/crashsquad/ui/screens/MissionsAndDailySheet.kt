package com.arovastudio.crashsquad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.arovastudio.crashsquad.data.DailyMissionEntity
import com.arovastudio.crashsquad.ui.theme.CoinGold
import com.arovastudio.crashsquad.ui.theme.GemPurple

@Composable
fun MissionsDialog(
    missions: List<DailyMissionEntity>,
    onClaimMission: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .border(2.5.dp, Color(0xFFFFB300), RoundedCornerShape(24.dp))
                .testTag("dialog_missions")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "DAILY MISSIONS",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFE65100)
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(missions) { mission ->
                        MissionRowItem(mission = mission, onClaim = { onClaimMission(mission.missionId) })
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MissionRowItem(
    mission: DailyMissionEntity,
    onClaim: () -> Unit
) {
    val isComplete = mission.currentCount >= mission.targetCount
    val progressFraction = (mission.currentCount.toFloat() / mission.targetCount).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFDE7))
            .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mission.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF3E2723)
                )

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progressFraction },
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${mission.currentCount} / ${mission.targetCount}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF757575)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (mission.isClaimed) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0E0E0))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Claimed", color = Color(0xFF757575), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            } else if (isComplete) {
                Button(
                    onClick = onClaim,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Claim", fontWeight = FontWeight.Bold, color = Color.White)
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x22000000))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    if (mission.rewardCoins > 0) {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = CoinGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+${mission.rewardCoins}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Diamond, contentDescription = null, tint = GemPurple, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+${mission.rewardGems}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class DailyRewardItem(
    val day: Int,
    val title: String,
    val coins: Int,
    val gems: Int,
    val isSpecial: Boolean = false
)

@Composable
fun DailyRewardDialog(
    currentStreak: Int,
    onClaimReward: (day: Int, coins: Int, gems: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val rewards = listOf(
        DailyRewardItem(1, "Day 1", coins = 100, gems = 0),
        DailyRewardItem(2, "Day 2", coins = 200, gems = 0),
        DailyRewardItem(3, "Day 3", coins = 0, gems = 3),
        DailyRewardItem(4, "Day 4", coins = 350, gems = 0),
        DailyRewardItem(5, "Day 5", coins = 0, gems = 5),
        DailyRewardItem(6, "Day 6", coins = 500, gems = 2),
        DailyRewardItem(7, "Day 7", coins = 1000, gems = 10, isSpecial = true)
    )

    val todayReward = rewards.find { it.day == currentStreak } ?: rewards.first()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .shadow(20.dp, RoundedCornerShape(26.dp))
                .border(3.dp, Color(0xFFAB47BC), RoundedCornerShape(26.dp))
                .testTag("dialog_daily_reward")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "7-DAY ISLAND REWARD",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF6A1B9A)
                )

                Text(
                    text = "Come back daily for tropical bonus treasures!",
                    fontSize = 12.sp,
                    color = Color(0xFF546E7A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rewards.take(4).forEach { item ->
                        DayRewardCard(item = item, currentStreak = currentStreak)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rewards.drop(4).forEach { item ->
                        DayRewardCard(item = item, currentStreak = currentStreak)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onClaimReward(todayReward.day, todayReward.coins, todayReward.gems)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(54.dp)
                        .shadow(6.dp, RoundedCornerShape(24.dp))
                        .testTag("btn_claim_daily")
                ) {
                    Text(
                        text = "Claim Day $currentStreak Reward",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun DayRewardCard(item: DailyRewardItem, currentStreak: Int) {
    val isPast = item.day < currentStreak
    val isToday = item.day == currentStreak
    val backgroundBrush = when {
        isToday -> Brush.verticalGradient(listOf(Color(0xFFE1BEE7), Color(0xFFBA68C8)))
        isPast -> Brush.verticalGradient(listOf(Color(0xFFE0E0E0), Color(0xFFD6D6D6)))
        else -> Brush.verticalGradient(listOf(Color(0xFFF5F5F5), Color(0xFFEEEEEE)))
    }

    Box(
        modifier = Modifier
            .size(width = 68.dp, height = 80.dp)
            .shadow(if (isToday) 6.dp else 2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundBrush)
            .border(
                width = if (isToday) 2.5.dp else 1.dp,
                color = if (isToday) Color(0xFF8E24AA) else Color(0x33000000),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = item.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isToday) Color.White else Color(0xFF455A64)
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (isPast) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
            } else if (item.gems > 0) {
                Icon(Icons.Default.Diamond, contentDescription = null, tint = if (isToday) Color.White else GemPurple, modifier = Modifier.size(22.dp))
                Text("+${item.gems}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = if (isToday) Color.White else Color.Black)
            } else {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = if (isToday) Color(0xFFFFD54F) else CoinGold, modifier = Modifier.size(22.dp))
                Text("+${item.coins}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = if (isToday) Color.White else Color.Black)
            }
        }
    }
}
