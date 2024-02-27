package com.orangeink.taskstodocompose.di

import com.orangeink.taskstodocompose.data.TasksRepositoryImpl
import com.orangeink.taskstodocompose.domain.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTasksRepository(impl: TasksRepositoryImpl): TasksRepository
}