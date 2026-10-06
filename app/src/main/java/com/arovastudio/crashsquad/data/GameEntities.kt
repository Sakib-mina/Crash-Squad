package com.arovastudio.crashsquad.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_progress")
data class GameProgressEntity(
    @PrimaryKey val id: Int = 1,
    val coins: Int = 1250,
    val gems: Int = 50,
    val currentLevel: Int = 1,
    val highestUnlockedLevel: Int = 1,
    val selectedHeroId: String = "blaze",
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val selectedLanguage: String = "English",
    val lastDailyRewardTimestamp: Long = 0L,
    val dailyRewardDayStreak: Int = 1,
    val totalBlocksDestroyed: Int = 0,
    val totalLevelsWon: Int = 0
)

@Entity(tableName = "level_records")
data class LevelRecordEntity(
    @PrimaryKey val levelId: Int,
    val stars: Int = 0,
    val highScore: Int = 0,
    val isCompleted: Boolean = false
)

@Entity(tableName = "hero_progress")
data class HeroProgressEntity(
    @PrimaryKey val heroId: String,
    val level: Int = 1,
    val isUnlocked: Boolean = false
)

@Entity(tableName = "daily_missions")
data class DailyMissionEntity(
    @PrimaryKey val missionId: String,
    val title: String,
    val currentCount: Int,
    val targetCount: Int,
    val rewardCoins: Int,
    val rewardGems: Int,
    val isClaimed: Boolean
)
