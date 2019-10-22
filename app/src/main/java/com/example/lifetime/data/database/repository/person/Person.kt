package com.example.lifetime.data.database.repository.person

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "persons")
data class Person(

    @Expose
    @ColumnInfo(name = "name")
    var name: String,

    @Expose
    @ColumnInfo(name = "Life_expectancy_years")
    @SerializedName("Life_expectancy_years")
    var LifeExpectancyYears: Float,


    @SerializedName("birth_date")
    @ColumnInfo(name = "birth_date")
    var birthDate: Long,

    @ColumnInfo(name = "country")
    var country: String? = null,

    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
)