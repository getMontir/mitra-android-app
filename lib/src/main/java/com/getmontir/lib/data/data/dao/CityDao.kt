package com.getmontir.lib.data.data.dao

import androidx.room.*
import com.getmontir.lib.data.data.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM CityEntity ORDER BY lastRefreshed DESC LIMIT 30")
    fun getAll(): Flow<List<CityEntity>>

    @Query("SELECT * FROM CityEntity WHERE id = :id")
    fun getById( id: String ): Flow<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(vararg provinces: CityEntity)

    @Update
    fun updates(vararg provinces: CityEntity)

    @Delete
    fun deletes(vararg provinces: CityEntity)

}