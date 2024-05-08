package com.example.notesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.LayoutRvNotesBinding
import com.example.notesapp.models.local.Note

class NotesAdapter(private val listener : OnItemClickListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: LayoutRvNotesBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutRvNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        // Setting click listeners and text data in ui
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

    override fun getItemCount() = differ.currentList.size

    /*fun refresh(data: MutableList<Note>) {
        notesList.clear()
        notesList.addAll(data)
        notifyDataSetChanged()
    }*/

    /**
     * Submits a new list of movies to be displayed by the adapter.
     * @param moviesList The new list of movies.
     */
    fun submitList(moviesList : List<Note>?){
        differ.submitList(moviesList)
    }

    interface OnItemClickListener{
        fun onItemClick(note : Note)
        fun onDeleteClick(note : Note)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(
            oldItem: Note,
            newItem: Note
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Note,
            newItem: Note
        ) = oldItem == newItem

    }
    private val differ = AsyncListDiffer(this, differCallback)
}