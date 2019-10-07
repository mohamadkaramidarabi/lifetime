package com.example.lifetime.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import javax.inject.Inject

class PersonAdapter @Inject constructor() : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    var persons: List<Person>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = persons?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        if (persons != null) holder.bind(persons?.get(position)) else Unit


    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val fullName: TextView = view.findViewById(R.id.fullName)
        private val delete: ImageView = view.findViewById(R.id.delete)
        private val edit: ImageView = view.findViewById(R.id.edit)

        init {
            delete.setOnClickListener {}
            edit.setOnClickListener {}
        }

        fun bind(person: Person?) {
            fullName.text = person?.name ?: ""
        }
    }
}