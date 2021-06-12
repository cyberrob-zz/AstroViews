package com.example.nasagallery.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nasagallery.database.entities.NasaImageRecord

@Dao
interface TheDao {

    @Insert
    suspend fun insertImage(image: NasaImageRecord): Long

    @Query("SELECT * from nasa_image")
    suspend fun getAllImages(): List<NasaImageRecord>

    // TODO: 2021/6/12 Find the correct syntax to query paged data
    @Query("SELECT * FROM nasa_image WHERE _id IN (SELECT _id FROM nasa_image ORDER BY _id LIMIT :size) ")
    suspend fun getImages(size: Int): List<NasaImageRecord>

    @Query("SELECT * from nasa_image WHERE _id = :id")
    suspend fun getImageById(id: Int): NasaImageRecord?

    @Query("DELETE FROM nasa_image")
    suspend fun deleteAllImages()
}