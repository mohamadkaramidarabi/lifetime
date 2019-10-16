package com.example.lifetime.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.lifetime.data.database.AppDatabase
import com.example.lifetime.data.database.AppDbHelper
import com.example.lifetime.data.database.DbHelper
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectanciesDao
import com.example.lifetime.data.database.repository.person.PersonDao
import com.example.lifetime.di.PreferenceInfo
import com.example.lifetime.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import info.vazeh.android.data.preferences.AppPreferenceHelper
import info.vazeh.android.data.preferences.PreferenceHelper
import io.reactivex.disposables.CompositeDisposable
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
    internal fun providePersonDao(appDatabase: AppDatabase): PersonDao = appDatabase.personDao()

    @Provides
    @Singleton
    internal fun provideLifeExpectancyDao(appDatabase: AppDatabase): LifeExpectanciesDao = appDatabase.lifeExpectanciesDao()


    @Provides
    internal fun provideCompositeDisposal(): CompositeDisposable = CompositeDisposable()

    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider = SchedulerProvider()

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper = appDbHelper

    @Singleton
    @Provides
    internal fun providePreferenceHelper(appPreferenceHelper: AppPreferenceHelper): PreferenceHelper =
        appPreferenceHelper


    @Provides
    @PreferenceInfo
    internal fun provideAppDbName() = "My_database"


}