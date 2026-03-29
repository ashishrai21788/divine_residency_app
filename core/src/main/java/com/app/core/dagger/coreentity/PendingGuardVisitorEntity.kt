package com.app.core.dagger.coreentity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pending_guard_visitors",
    indices = [Index(value = ["client_request_id"], unique = true)]
)
data class PendingGuardVisitorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "client_request_id") val clientRequestId: String,
    @ColumnInfo(name = "request_json") val requestJson: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    /** PENDING or FAILED (retry eligible) */
    @ColumnInfo(name = "sync_status") val syncStatus: String,
    @ColumnInfo(name = "retry_count") val retryCount: Int = 0,
    @ColumnInfo(name = "last_error") val lastError: String? = null
)
