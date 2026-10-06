package com.arovastudio.crashsquad.data

import com.arovastudio.crashsquad.game.GameLevels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GameRepository(private val dao: GameDao) {

    val gameProgress: Flow<GameProgressEntity?> = dao.getGameProgress()
    val levelRecords: Flow<List<LevelRecordEntity>> = dao.getAllLevelRecords()
    val heroProgress: Flow<List<HeroProgressEntity>> = dao.getAllHeroProgress()
    val dailyMissions: Flow<List<DailyMissionEntity>> = dao.getAllMissions()

    suspend fun initializeDefaultsIfNeeded() {
        val current = dao.getGameProgress().firstOrNull()
        if (current == null) {
            dao.saveGameProgress(
                GameProgressEntity(
                    id = 1,
                    coins = 1250,
                    gems = 50,
                    currentLevel = 1,
                    highestUnlockedLevel = 1,
                    selectedHeroId = "blaze",
                    soundEnabled = true,
                    musicEnabled = true,
                    vibrationEnabled = true,
                    selectedLanguage = "English",
                    lastDailyRewardTimestamp = 0L,
                    dailyRewardDayStreak = 1
                )
            )

            val initialLevels = (1..50).map { levelId ->
                LevelRecordEntity(
                    levelId = levelId,
                    stars = 0,
                    highScore = 0,
                    isCompleted = false
                )
            }
            dao.saveLevelRecords(initialLevels)

            val initialHeroes = listOf(
                HeroProgressEntity(heroId = "blaze", level = 1, isUnlocked = true),
                HeroProgressEntity(heroId = "aqua", level = 1, isUnlocked = false),
                HeroProgressEntity(heroId = "rocky", level = 1, isUnlocked = false),
                HeroProgressEntity(heroId = "leaf", level = 1, isUnlocked = false)
            )
            dao.saveAllHeroProgress(initialHeroes)

            val initialMissions = listOf(
                DailyMissionEntity("m1", "Complete 3 levels", currentCount = 0, targetCount = 3, rewardCoins = 100, rewardGems = 0, isClaimed = false),
                DailyMissionEntity("m2", "Destroy 20 blocks", currentCount = 0, targetCount = 20, rewardCoins = 0, rewardGems = 2, isClaimed = false),
                DailyMissionEntity("m3", "Win 1 level", currentCount = 0, targetCount = 1, rewardCoins = 50, rewardGems = 0, isClaimed = false)
            )
            dao.saveAllMissions(initialMissions)
        }
    }

    suspend fun addCurrency(coinsDelta: Int, gemsDelta: Int) {
        val current = dao.getGameProgress().firstOrNull() ?: GameProgressEntity()
        dao.saveGameProgress(
            current.copy(
                coins = (current.coins + coinsDelta).coerceAtLeast(0),
                gems = (current.gems + gemsDelta).coerceAtLeast(0)
            )
        )
    }

    suspend fun completeLevel(levelId: Int, starsEarned: Int, scoreEarned: Int, coinsReward: Int, gemsReward: Int, blocksDestroyedCount: Int) {
        val current = dao.getGameProgress().firstOrNull() ?: GameProgressEntity()
        val nextLevel = levelId + 1
        val maxLevels = GameLevels.ALL_LEVELS.size
        val updatedHighest = maxOf(current.highestUnlockedLevel, minOf(maxLevels, nextLevel))
        dao.saveGameProgress(
            current.copy(
                coins = current.coins + coinsReward,
                gems = current.gems + gemsReward,
                highestUnlockedLevel = updatedHighest,
                currentLevel = minOf(maxLevels, nextLevel),
                totalBlocksDestroyed = current.totalBlocksDestroyed + blocksDestroyedCount,
                totalLevelsWon = current.totalLevelsWon + 1
            )
        )

        dao.saveLevelRecord(
            LevelRecordEntity(
                levelId = levelId,
                stars = starsEarned,
                highScore = scoreEarned,
                isCompleted = true
            )
        )

        val missions = dao.getAllMissions().firstOrNull() ?: emptyList()
        val updatedMissions = missions.map { m ->
            when (m.missionId) {
                "m1" -> m.copy(currentCount = minOf(m.targetCount, m.currentCount + 1))
                "m2" -> m.copy(currentCount = minOf(m.targetCount, m.currentCount + blocksDestroyedCount))
                "m3" -> m.copy(currentCount = minOf(m.targetCount, m.currentCount + 1))
                else -> m
            }
        }
        dao.saveAllMissions(updatedMissions)
    }

    suspend fun upgradeHero(heroId: String, coinCost: Int): Boolean {
        val current = dao.getGameProgress().firstOrNull() ?: return false
        if (current.coins < coinCost) return false

        val heroes = dao.getAllHeroProgress().firstOrNull() ?: return false
        val targetHero = heroes.find { it.heroId == heroId } ?: return false

        dao.saveGameProgress(current.copy(coins = current.coins - coinCost))
        dao.saveHeroProgress(targetHero.copy(level = targetHero.level + 1))
        return true
    }

    suspend fun unlockHero(heroId: String, gemCost: Int): Boolean {
        val current = dao.getGameProgress().firstOrNull() ?: return false
        if (current.gems < gemCost) return false

        val heroes = dao.getAllHeroProgress().firstOrNull() ?: return false
        val targetHero = heroes.find { it.heroId == heroId } ?: return false

        dao.saveGameProgress(current.copy(gems = current.gems - gemCost))
        dao.saveHeroProgress(targetHero.copy(isUnlocked = true))
        return true
    }

    suspend fun selectHero(heroId: String) {
        val current = dao.getGameProgress().firstOrNull() ?: return
        dao.saveGameProgress(current.copy(selectedHeroId = heroId))
    }

    suspend fun updateSettings(sound: Boolean? = null, music: Boolean? = null, vibration: Boolean? = null, language: String? = null) {
        val current = dao.getGameProgress().firstOrNull() ?: return
        dao.saveGameProgress(
            current.copy(
                soundEnabled = sound ?: current.soundEnabled,
                musicEnabled = music ?: current.musicEnabled,
                vibrationEnabled = vibration ?: current.vibrationEnabled,
                selectedLanguage = language ?: current.selectedLanguage
            )
        )
    }

    suspend fun claimMission(missionId: String): Boolean {
        val missions = dao.getAllMissions().firstOrNull() ?: return false
        val mission = missions.find { it.missionId == missionId } ?: return false
        if (mission.isClaimed || mission.currentCount < mission.targetCount) return false

        dao.saveMission(mission.copy(isClaimed = true))
        addCurrency(mission.rewardCoins, mission.rewardGems)
        return true
    }

    suspend fun claimDailyReward(rewardDay: Int, coins: Int, gems: Int) {
        val current = dao.getGameProgress().firstOrNull() ?: return
        val nextStreak = if (rewardDay >= 7) 1 else rewardDay + 1
        dao.saveGameProgress(
            current.copy(
                coins = current.coins + coins,
                gems = current.gems + gems,
                dailyRewardDayStreak = nextStreak,
                lastDailyRewardTimestamp = System.currentTimeMillis()
            )
        )
    }
}
