package com.example.astroviews.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.astroviews.database.entities.AstroImageRecord

@Dao
interface TheDao {

    @Insert
    suspend fun insertImage(image: AstroImageRecord): Long

    @Query("SELECT * from astro_image_record")
    suspend fun getAllImages(): List<AstroImageRecord>

    @Query("SELECT * FROM astro_image_record WHERE _id NOT IN ( SELECT _id FROM astro_image_record ORDER BY date DESC LIMIT :offset) ORDER BY date DESC LIMIT :size")
    suspend fun getImages(offset: Int, size: Int): List<AstroImageRecord>

    @Query("SELECT * from astro_image_record WHERE _id = :id")
    suspend fun getImageById(id: Int): AstroImageRecord?

    @Query("DELETE FROM astro_image_record")
    suspend fun deleteAllImages()
}