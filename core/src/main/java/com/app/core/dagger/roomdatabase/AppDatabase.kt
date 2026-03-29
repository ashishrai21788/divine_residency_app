package com.app.core.dagger.roomdatabase


import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.core.dagger.coredao.PendingGuardVisitorDao
import com.app.core.dagger.coredao.UserDao
import com.app.core.dagger.coreentity.PendingGuardVisitorEntity
import com.app.core.dagger.coreentity.User

@Database(entities = [User::class, PendingGuardVisitorEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun pendingGuardVisitorDao(): PendingGuardVisitorDao
}