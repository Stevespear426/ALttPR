package com.stingers.alttpr.repository.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.stingers.alttpr.model.LogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Dao
abstract class LoggerDao {

    @Query("""
        SELECT * FROM LogEntity 
        WHERE (:type IS NULL OR type = :type) 
        AND (:search = '' OR rowid IN (SELECT rowid FROM LogSearchIndex WHERE LogSearchIndex MATCH :search)) 
        ORDER BY timestamp DESC LIMIT 500
    """)
    abstract fun getLogs(type: String?, search: String): Flow<List<LogEntity>>

    @Insert
    abstract suspend fun insertLogInternal(log: LogEntity)

    @Query("DELETE FROM LogEntity")
    abstract suspend fun deleteAllLogsInternal()

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    fun insertLog(log: LogEntity) {
        scope.launch {
            insertLogInternal(log)
        }
    }

    fun deleteAllLogs() {
        scope.launch {
            deleteAllLogsInternal()
        }
    }
}