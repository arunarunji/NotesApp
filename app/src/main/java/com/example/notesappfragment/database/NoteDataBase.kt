package com.example.notesappfragment.database

import android.app.Application
import androidx.room.*
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.noteDao.NoteDao


@Database(entities = [Notes::class], version = 2, exportSchema = true)
abstract class NoteDataBase: RoomDatabase() {

    abstract fun getNotesDao(): NoteDao


    companion object
    {
        @Volatile

        private var INSTANCE:NoteDataBase?=null
        fun getDatabase(application: Application):NoteDataBase
        {
            return INSTANCE?: synchronized(this) {
                val instance= Room.databaseBuilder(
                    application.applicationContext,
                    NoteDataBase::class.java,
                    "note_database"


                ).fallbackToDestructiveMigration().build()
                INSTANCE=instance
                instance
            }
        }
    }

}