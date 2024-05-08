package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.adapters.NotesAdapter
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.models.local.Note
import com.example.notesapp.models.local.NoteDatabase
import com.example.notesapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NotesAdapter.OnItemClickListener {
    private val noteDatabase by lazy { NoteDatabase.getDatabase(this).noteDao() }
    private lateinit var binding : ActivityMainBinding
    private var currentAddedNotes = mutableListOf<Note>()
    private lateinit var notesAdapter : NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        setUpListeners()

    }

    /**
     * Initializes and configures the RecyclerView with a NotesAdapter and a LinearLayoutManager
     */
    private fun setUpRecyclerView() {
        notesAdapter = NotesAdapter(this)
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }
    }

    /**
     * Sets up listeners for various UI elements
     */
    private fun setUpListeners() {
        binding.idFABAdd.setOnClickListener {
            startNoteActivity(Constants.Add, null)
        }
    }

    /**
     * Launches NoteActivity with specific parameters for adding or editing notes
     */
    private fun startNoteActivity(noteType: String, note: Note?) {
        val intent = Intent(this, NoteActivity::class.java).apply {
            putExtra(Constants.NoteType, noteType)
            note?.let { putExtra(Constants.Note, it) }
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    /**
     * Updates the user interface by fetching notes from the database and submitting them to the adapter
     */
    private fun updateUi() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                currentAddedNotes = noteDatabase.getNotes().toMutableList()
            }
            withContext(Dispatchers.Main){
                notesAdapter.submitList(currentAddedNotes.toList())
            }
        }
    }

    override fun onItemClick(note: Note) {
        startNoteActivity(Constants.Edit, note)
    }

    override fun onDeleteClick(note: Note) {
        lifecycleScope.launch {
            noteDatabase.deleteNote(note)
            currentAddedNotes.remove(note)
            notesAdapter.submitList(currentAddedNotes.toList())
            // updateUi()
        }
    }
}