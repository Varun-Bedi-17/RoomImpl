package com.example.notesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.LayoutRvNotesBinding
import com.example.notesapp.models.local.Note

class NotesAdapter(private val listener : OnItemClickListener, private var notesList : MutableList<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: LayoutRvNotesBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutRvNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = notesList[position]
        holder.binding.apply {
            tvNote.text = data.noteText
            cvNotes.setOnClickListener {
                listener.onItemClick(data)
            }
            ivDelete.setOnClickListener{
                listener.onDeleteClick(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun refresh(data: MutableList<Note>) {
        notesList.clear()
        notesList.addAll(data)
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(note : Note)
        fun onDeleteClick(note : Note)
    }
}