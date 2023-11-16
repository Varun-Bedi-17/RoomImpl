package com.example.notesapp.models.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): List<Note>

    @Update
    suspend fun editNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

}