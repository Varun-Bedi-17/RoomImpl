package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.databinding.ActivityNoteBinding
import com.example.notesapp.models.local.Note
import com.example.notesapp.models.local.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNoteBinding
    private val noteDatabase by lazy { NoteDatabase.getDatabase(this).noteDao() }
    private var noteDetail : Note? = null
    private var noteType : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDetail = intent.getSerializableExtra("Note") as Note?
        noteType = intent.getStringExtra("NoteType")
        if(noteType == "Edit") {
            binding.saveBtn.text = getString(R.string.update)
            binding.tvNote.setText(noteDetail?.noteText)

        }

        setUpListeners()
    }

    private fun setUpListeners() {

        binding.deleteBtn.setOnClickListener {
            lifecycleScope.launch {
                noteDetail?.let { it1 -> noteDatabase.deleteNote(it1) }
            }
        }
        binding.updateBtn.setOnClickListener {

        }
        binding.saveBtn.setOnClickListener {
            if(noteType == "Add") {
                lifecycleScope.launch {
                    noteDatabase.addNote(Note(null, binding.tvNote.text.toString()))
                }
            }else{
                lifecycleScope.launch {
                    val updatedNote = Note(noteDetail?.id, binding.tvNote.text.toString())
                    noteDatabase.editNote(updatedNote)
                }
            }
            finish()
        }
    }
}