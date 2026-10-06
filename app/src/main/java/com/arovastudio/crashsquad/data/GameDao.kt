package com.arovastudio.crashsquad.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game_progress WHERE id = 1 LIMIT 1")
    fun getGameProgress(): Flow<GameProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGameProgress(progress: GameProgressEntity)

    @Query("SELECT * FROM level_records ORDER BY levelId ASC")
    fun getAllLevelRecords(): Flow<List<LevelRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLevelRecord(levelRecord: LevelRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLevelRecords(levelRecords: List<LevelRecordEntity>)

    @Query("SELECT * FROM hero_progress")
    fun getAllHeroProgress(): Flow<List<HeroProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHeroProgress(heroProgress: HeroProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllHeroProgress(list: List<HeroProgressEntity>)

    @Query("SELECT * FROM daily_missions")
    fun getAllMissions(): Flow<List<DailyMissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMission(mission: DailyMissionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllMissions(missions: List<DailyMissionEntity>)
}
