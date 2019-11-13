package com.example.lifetime.data.database.repository.person

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "persons")
data class Person(

    @Expose
    @ColumnInfo(name = "name")
    var name: String,

    @Expose
    @ColumnInfo(name = "Life_expectancy_years")
    @SerializedName("Life_expectancy_years")
    var lifeExpectancyYears: Float,


    @SerializedName("birth_date")
    @ColumnInfo(name = "birth_date")
    var birthDate: Long,

    @ColumnInfo(name = "country")
    var country: String? = null


) : Serializable{

    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @Expose
    @ColumnInfo(name = "is_main_user")
    @SerializedName("is_main_user")
    var isMainUser = false

    @Expose
    @ColumnInfo(name = "email")
    var email: String? = null
        get() = if (!isMainUser) null else field
}