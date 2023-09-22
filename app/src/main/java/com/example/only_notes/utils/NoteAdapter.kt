package com.example.only_notes.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.only_notes.databinding.RecyclerViewHomeNotesDisplayBinding
import com.example.only_notes.fragments.AddNotesPopUpFragment

class NoteAdapter(private val list : MutableList<NotesData>) :
    RecyclerView.Adapter<NoteAdapter.NotesViewHolder>(){

    private var listener : NotesAdapterClicksInterface? = null

    fun setListener(listener : NotesAdapterClicksInterface){
        this.listener = listener
    }



    inner class NotesViewHolder(val binding : RecyclerViewHomeNotesDisplayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder  {

        val binding  = RecyclerViewHomeNotesDisplayBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        with(holder){
            with(list[position]){

                binding.NotesView.text = this.notesData

                binding.editPen.setOnClickListener {
                    listener?.onEditTaskBtnClick(this)
                }

                binding.deletePen.setOnClickListener {
                    listener?.onEditTaskBtnClick(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface  NotesAdapterClicksInterface {
        fun onDeleteTaskBtnClick(notesData: NotesData)
        fun onEditTaskBtnClick(notesData: NotesData)
    }
}