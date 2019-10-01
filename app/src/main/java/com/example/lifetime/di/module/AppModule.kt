package com.example.lifetime.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.lifetime.data.database.AppDatabase
import com.example.lifetime.data.database.repository.person.PersonDao
import com.example.lifetime.data.database.repository.person.PersonRepo
import com.example.lifetime.data.database.repository.person.PersonRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "App Database").build()

    @Provides
    @Singleton
    internal fun providePersonRepoHelper(personRepository: PersonRepository) : PersonRepo
            = personRepository

    @Provides
    @Singleton
    internal fun providePersonDao(appDatabase: AppDatabase) : PersonDao
            = appDatabase.personDao()
}