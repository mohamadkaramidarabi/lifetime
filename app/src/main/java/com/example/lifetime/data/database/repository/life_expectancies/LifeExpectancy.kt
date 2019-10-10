package com.example.lifetime.data.database.repository.life_expectancies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "life_expectancy")
data class LifeExpectancy(
    @Expose
    @ColumnInfo(name = "country")
    var country: String,

    @Expose
    @ColumnInfo(name = "life_Expectancy")
    @SerializedName("expectancy")
    var lifeExpectancy: Float
){
    @Expose
    @PrimaryKey(autoGenerate = true)
    var countryId: Int = 0
}