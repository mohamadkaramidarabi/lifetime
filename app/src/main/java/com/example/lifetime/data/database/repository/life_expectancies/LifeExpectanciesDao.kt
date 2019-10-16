package com.example.lifetime.data.database.repository.life_expectancies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface LifeExpectanciesDao {

    @Query("SELECT * FROM life_expectancy")
    fun loadAll(): List<LifeExpectancy>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(lifeExpectancies: List<LifeExpectancy>)
}