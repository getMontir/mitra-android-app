package com.getmontir.lib.data.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.getmontir.lib.data.data.converter.DateConverters
import com.getmontir.lib.data.data.dao.CityDao
import com.getmontir.lib.data.data.dao.DistrictDao
import com.getmontir.lib.data.data.dao.ProvinceDao
import com.getmontir.lib.data.data.entity.CityEntity
import com.getmontir.lib.data.data.entity.DistrictEntity
import com.getmontir.lib.data.data.entity.ProvinceEntity

@Database(entities = [
    ProvinceEntity::class,
    CityEntity::class,
    DistrictEntity::class
], version = 1, exportSchema = true)
@TypeConverters(DateConverters::class)
abstract class AppDatabase: RoomDatabase() {

    // DAO
    abstract fun provinceDao(): ProvinceDao
    abstract fun cityDao(): CityDao
    abstract fun districtDao(): DistrictDao

    companion object {
        fun buildDatabase( context: Context ): AppDatabase {
            return Room
                .databaseBuilder(context.applicationContext, AppDatabase::class.java, "getMontirApp")
                .build()
        }
    }
}