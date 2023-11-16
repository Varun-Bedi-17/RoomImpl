package com.example.notesapp.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int?,
    @ColumnInfo(name = "noteText")
    val noteText: String
) :java.io.Serializable