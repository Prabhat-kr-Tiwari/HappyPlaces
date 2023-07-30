package com.example.happyplaces.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.happyplaces.Model.PlaceEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaceDao {

    @Insert
    suspend fun insert(placeentity: PlaceEntity)

    @Query("Select * from `Place_table`")
    fun getAll(): Flow< List<PlaceEntity>>


}