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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
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
        binding.idFABAdd.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra("NoteType", "Add")
            startActivity(intent)
        }

    }

    private fun setUpRecyclerView() {
        notesAdapter = NotesAdapter(this@MainActivity, currentAddedNotes)
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }
    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("NoteType", "Edit")
        intent.putExtra("Note", note)
        startActivity(intent)
    }

    override fun onDeleteClick(note: Note) {
        lifecycleScope.launch {
            noteDatabase.deleteNote(note)
            updateUi()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun updateUi() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                currentAddedNotes = noteDatabase.getNotes().toMutableList()
            }
            withContext(Dispatchers.Main){
                notesAdapter.refresh(currentAddedNotes)
            }
        }
    }
}