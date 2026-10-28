package com.example.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "cached_app_data")
data class CachedData(
    @PrimaryKey val id: Int = 1,
    val configs: String = "",
    val configCount: Int = 0,
    val configTimestamp: String = "",
    val proxies: String = "",
    val proxyCount: Int = 0,
    val proxyTimestamp: String = "",
    val subLink: String = "",
    val dlA9z: String = "",
    val dlAndroid: String = "",
    val dlIos: String = "",
    val dlWin32: String = "",
    val dlWin64: String = "",
    val dlWsWindows: String = "",
    val dlWsAndroid: String = "",
    val dlWsIos: String = "",
    val dlWsMac: String = ""
)

@Entity(tableName = "cached_history")
data class CachedHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "CONFIG" or "PROXY"
    val content: String,
    val count: Int,
    val timestamp: String,
    val version: String
)

@Dao
interface CachedDataDao {
    @Query("SELECT * FROM cached_app_data WHERE id = 1")
    fun getCachedDataFlow(): Flow<CachedData?>

    @Query("SELECT * FROM cached_app_data WHERE id = 1")
    suspend fun getCachedDataOnce(): CachedData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedData(data: CachedData)

    @Query("SELECT * FROM cached_history WHERE type = :type ORDER BY id DESC")
    fun getHistoryFlow(type: String): Flow<List<CachedHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItems(items: List<CachedHistoryItem>)

    @Query("DELETE FROM cached_history WHERE type = :type")
    suspend fun deleteHistoryByType(type: String)
}

@Database(entities = [CachedData::class, CachedHistoryItem::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): CachedDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "a9z_v2ray_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
