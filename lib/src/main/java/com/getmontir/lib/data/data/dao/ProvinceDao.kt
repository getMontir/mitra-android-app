package com.getmontir.lib.data.data.dao

import androidx.room.*
import com.getmontir.lib.data.data.entity.ProvinceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProvinceDao {

    @Query("SELECT * FROM ProvinceEntity ORDER BY lastRefreshed DESC LIMIT 30")
    fun getAll(): List<ProvinceEntity>

    @Query("SELECT * FROM ProvinceEntity WHERE id = :id")
    fun getById( id: String ): ProvinceEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(vararg provinces: ProvinceEntity)

    @Update
    fun updates(vararg provinces: ProvinceEntity)

    @Delete
    fun deletes(vararg provinces: ProvinceEntity)

}