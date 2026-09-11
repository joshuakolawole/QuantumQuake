package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Query("SELECT * FROM saved_configurations ORDER BY timestamp DESC")
    fun getAllConfigs(): Flow<List<SavedConfigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: SavedConfigEntity): Long

    @Delete
    suspend fun deleteConfig(config: SavedConfigEntity)

    @Query("DELETE FROM saved_configurations WHERE id = :id")
    suspend fun deleteConfigById(id: Long)

    @Query("DELETE FROM saved_configurations")
    suspend fun deleteAllConfigs()
}
