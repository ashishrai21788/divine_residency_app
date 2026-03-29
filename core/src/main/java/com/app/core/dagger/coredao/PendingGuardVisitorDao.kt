package com.app.core.dagger.coredao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.core.dagger.coreentity.PendingGuardVisitorEntity

@Dao
interface PendingGuardVisitorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PendingGuardVisitorEntity): Long

    @Query("SELECT * FROM pending_guard_visitors WHERE sync_status = :status ORDER BY created_at ASC")
    suspend fun listByStatus(status: String): List<PendingGuardVisitorEntity>

    @Query("SELECT COUNT(*) FROM pending_guard_visitors WHERE sync_status = :status")
    fun observePendingCount(status: String): LiveData<Int>

    @Query("DELETE FROM pending_guard_visitors WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        "UPDATE pending_guard_visitors SET sync_status = :status, retry_count = retry_count + 1, last_error = :error WHERE id = :id"
    )
    suspend fun markFailed(id: Long, status: String, error: String?)
}
