package com.example.lifetime.di.module

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.lifetime.data.database.AppDatabase
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectanciesDao
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancyRepo
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancyRepository
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.data.database.repository.person.PersonDao
import com.example.lifetime.data.database.repository.person.PersonRepo
import com.example.lifetime.data.database.repository.person.PersonRepository
import com.example.lifetime.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Future
import javax.inject.Named
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
    internal fun providePersonRepoHelper(personRepository: PersonRepository): PersonRepo =
        personRepository

    @Provides
    @Singleton
    internal fun provideLifeExpectacyRepoHelper(lifeExpectancyRepository: LifeExpectancyRepository): LifeExpectancyRepo =
        lifeExpectancyRepository

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




}