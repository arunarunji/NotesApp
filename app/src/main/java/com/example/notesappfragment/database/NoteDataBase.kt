package com.example.notesappfragment.database

import android.app.Application
import androidx.room.*
import com.example.notesappfragment.Convertor
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.noteDao.NoteDao


@Database(entities = [Notes::class], version = 1, exportSchema = false)
@TypeConverters(Convertor::class)
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


                ).build()
                INSTANCE=instance
                instance
            }
        }
    }

}