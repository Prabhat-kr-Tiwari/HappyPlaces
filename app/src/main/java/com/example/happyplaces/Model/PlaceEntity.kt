package com.example.happyplaces.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName="Place_table")
data class PlaceEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    var title:String,
    var description:String,
    var date: String,
    var location:String,
    var image:ByteArray?
)
