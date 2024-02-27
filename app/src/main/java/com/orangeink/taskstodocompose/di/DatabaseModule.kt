package com.orangeink.taskstodocompose.di

import android.app.Application
import androidx.room.Room
import com.orangeink.taskstodocompose.data.db.TaskDatabase
import com.orangeink.taskstodocompose.data.db.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideUserDatabase(
        application: Application
    ): TaskDatabase {
        return Room.databaseBuilder(application, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.getTaskDao()
    }
}