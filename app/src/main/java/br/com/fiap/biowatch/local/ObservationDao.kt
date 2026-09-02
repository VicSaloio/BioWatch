package br.com.fiap.biowatch.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ObservationDao {

    @Insert
    suspend fun insert(observation: ObservationEntity): Long

    @Query("SELECT * FROM observations ORDER BY id DESC")
    suspend fun getAll(): List<ObservationEntity>

    @Query("DELETE FROM observations WHERE id = :id")
    suspend fun deleteById(id: Int)
}