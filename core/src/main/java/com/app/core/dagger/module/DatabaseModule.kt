package com.app.core.dagger.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.core.dagger.coredao.UserDao
import com.app.core.dagger.roomdatabase.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context.applicationContext,
            AppDatabase::class.java,
            "app_database")
            .fallbackToDestructiveMigration(false)
            .allowMainThreadQueries()
            .build()
    }


    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

}