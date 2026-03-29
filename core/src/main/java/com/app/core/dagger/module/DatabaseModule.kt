package com.app.core.dagger.module

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.core.dagger.coredao.PendingGuardVisitorDao
import com.app.core.dagger.coredao.UserDao
import com.app.core.dagger.roomdatabase.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DatabaseModule {

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `pending_guard_visitors` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `client_request_id` TEXT NOT NULL,
                    `request_json` TEXT NOT NULL,
                    `created_at` INTEGER NOT NULL,
                    `sync_status` TEXT NOT NULL,
                    `retry_count` INTEGER NOT NULL,
                    `last_error` TEXT
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_pending_guard_visitors_client_request_id` " +
                        "ON `pending_guard_visitors` (`client_request_id`)"
                )
            }
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_2_3)
            .fallbackToDestructiveMigration(false)
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Singleton
    @Provides
    fun providePendingGuardVisitorDao(db: AppDatabase): PendingGuardVisitorDao = db.pendingGuardVisitorDao()
}