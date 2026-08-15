package com.example.nicecode

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@Entity(tableName = "history_records")
internal data class HistoryRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: String,
    val timestamp: Long,
    val resultList: List<String>,
)

internal class HistoryRecordConverters {
    @TypeConverter
    fun fromResultList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toResultList(value: String): List<String> {
        if (value.isBlank()) {
            return emptyList()
        }
        return value.split(",")
    }
}

@Dao
internal interface HistoryRecordDao {
    @Insert
    suspend fun insert(record: HistoryRecordEntity)

    @Query(
        """
        SELECT * FROM history_records
        WHERE date >= :minDate
        ORDER BY date DESC, timestamp DESC, id DESC
        """
    )
    fun observeRecentRecords(minDate: String): Flow<List<HistoryRecordEntity>>

    @Query(
        """
        SELECT id FROM history_records
        WHERE date = :date
        ORDER BY timestamp DESC, id DESC
        LIMIT 2147483647 OFFSET :keepCount
        """
    )
    suspend fun findOverflowIds(
        date: String,
        keepCount: Int,
    ): List<Long>

    @Query("DELETE FROM history_records WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("DELETE FROM history_records WHERE date < :cutoffDate")
    suspend fun deleteBefore(cutoffDate: String)
}

@Database(
    entities = [HistoryRecordEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(HistoryRecordConverters::class)
internal abstract class NicecodeDatabase : RoomDatabase() {
    abstract fun historyRecordDao(): HistoryRecordDao

    companion object {
        private val migrationFrom1To2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS history_records_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        date TEXT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        resultList TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT INTO history_records_new (id, date, timestamp, resultList)
                    SELECT id, date, timestamp, resultList
                    FROM history_records
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE history_records")
                database.execSQL("ALTER TABLE history_records_new RENAME TO history_records")
            }
        }

        @Volatile
        private var instance: NicecodeDatabase? = null

        fun getInstance(context: Context): NicecodeDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NicecodeDatabase::class.java,
                    "nicecode.db"
                )
                    .addMigrations(migrationFrom1To2)
                    .build()
                    .also { database ->
                    instance = database
                }
            }
        }
    }
}

internal class HistoryRepository(context: Context) {
    private val database = NicecodeDatabase.getInstance(context)
    private val historyRecordDao = database.historyRecordDao()

    fun observeRecentRecords(currentDate: LocalDate): Flow<List<HistoryRecordEntity>> {
        val minDate = currentDate.minusDays(4).toString()
        return historyRecordDao.observeRecentRecords(minDate)
    }

    suspend fun recordHistory(
        results: List<String>,
        currentDate: LocalDate = LocalDate.now(),
        timestamp: Long = System.currentTimeMillis(),
    ) {
        if (results.isEmpty()) {
            return
        }

        val date = currentDate.toString()

        database.withTransaction {
            historyRecordDao.insert(
                HistoryRecordEntity(
                    date = date,
                    timestamp = timestamp,
                    resultList = results
                )
            )
            val overflowIds = historyRecordDao.findOverflowIds(date = date, keepCount = 3)
            if (overflowIds.isNotEmpty()) {
                historyRecordDao.deleteByIds(overflowIds)
            }
        }
    }

    suspend fun cleanupExpiredRecords(currentDate: LocalDate = LocalDate.now()) {
        val cutoffDate = currentDate.minusDays(4).toString()
        historyRecordDao.deleteBefore(cutoffDate)
    }
}
