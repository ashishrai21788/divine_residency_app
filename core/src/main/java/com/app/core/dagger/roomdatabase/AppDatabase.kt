package com.app.core.dagger.roomdatabase


import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.core.dagger.coredao.UserDao
import com.app.core.dagger.coreentity.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}