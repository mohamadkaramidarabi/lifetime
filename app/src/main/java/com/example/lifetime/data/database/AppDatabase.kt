package com.example.lifetime.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectanciesDao
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.data.database.repository.person.PersonDao
import com.example.lifetime.util.DB_NAME

@Database(entities = [
    Person::class,
    LifeExpectancy::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao
    abstract fun lifeExpectanciesDao(): LifeExpectanciesDao


    companion object {
        private  var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            var localInstance: AppDatabase? = INSTANCE
            if (localInstance == null) {
                synchronized(AppDatabase::class.java) {
                    localInstance = INSTANCE
                    if (localInstance == null) {
                        localInstance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                            .allowMainThreadQueries()
                            .build()

                        INSTANCE = localInstance
                    }
                }
            }
            return localInstance!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}