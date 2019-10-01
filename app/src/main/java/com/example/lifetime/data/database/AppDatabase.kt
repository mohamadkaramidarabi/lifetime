package com.example.lifetime.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.data.database.repository.person.PersonDao

@Database(entities = [Person::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}