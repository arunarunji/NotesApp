package com.example.notesappfragment.noteDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesappfragment.entities.Notes
@Dao
interface NoteDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Notes)

    @Update
    suspend fun update(note: Notes)

    @Query("DELETE FROM Notes where id=:id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM notes order by id asc")
    fun getAllNotes(): LiveData<List<Notes>>

}