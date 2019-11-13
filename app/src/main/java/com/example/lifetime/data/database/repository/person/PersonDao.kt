package com.example.lifetime.data.database.repository.person

import androidx.room.*
import io.reactivex.Observable

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: Person)

    @Query("SELECT * FROM persons")
    fun loadAll(): List<Person>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePerson(vararg persons: Person): Int

    @Delete
    fun deletePerson(vararg person: Person)
}