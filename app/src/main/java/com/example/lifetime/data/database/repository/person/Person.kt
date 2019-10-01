package com.example.lifetime.data.database.repository.person

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "persons")
data class Person(
    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @Expose
    @ColumnInfo(name = "name")
    var name: String,

    @Expose
    @ColumnInfo(name = "age")
    var age: Int
)