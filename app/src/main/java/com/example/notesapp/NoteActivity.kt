package com.example.notesapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.databinding.ActivityNoteBinding
import com.example.notesapp.models.local.Note
import com.example.notesapp.models.local.NoteDatabase
import com.example.notesapp.utils.Constants
import kotlinx.coroutines.launch

class NoteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNoteBinding
    private val noteDatabase by lazy { NoteDatabase.getDatabase(this).noteDao() }
    private var noteDetail : Note? = null
    private var noteType : String? = null


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDetail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Constants.Note, Note::class.java)
        }else{
            intent.getSerializableExtra(Constants.Note) as Note?
        }
        noteType = intent.getStringExtra(Constants.NoteType)
        if(noteType == Constants.Edit) {
            binding.saveBtn.text = getString(R.string.update)
            binding.tvNote.setText(noteDetail?.noteText)

        }

        setUpListeners()
    }

    /**
     * Sets up listeners for various UI elements
     */
    private fun setUpListeners() {
        binding.saveBtn.setOnClickListener {
            if(noteType == Constants.Add) {
                lifecycleScope.launch {
                    noteDatabase.addNote(Note(null, binding.tvNote.text.toString()))
                }
            }else{
                lifecycleScope.launch {
                    val updatedNote = Note(noteDetail?.id, binding.tvNote.text.toString())
                    noteDatabase.editNote(updatedNote)
                }
            }
            setResult(RESULT_OK)
            finish()
        }
    }
}