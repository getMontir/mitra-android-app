package com.getmontir.lib.data.data.dao

import androidx.room.*
import com.getmontir.lib.data.data.entity.DistrictEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DistrictDao {

    @Query("SELECT * FROM DistrictEntity ORDER BY lastRefreshed DESC LIMIT 30")
    fun getAll(): Flow<List<DistrictEntity>>

    @Query("SELECT * FROM DistrictEntity WHERE id = :id")
    fun getById( id: String ): Flow<DistrictEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(vararg provinces: DistrictEntity)

    @Update
    fun updates(vararg provinces: DistrictEntity)

    @Delete
    fun deletes(vararg provinces: DistrictEntity)

}