package com.example.lifetime.ui.main.main_activity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.addperson.AddPersonDialog
import javax.inject.Inject

class PersonAdapter @Inject constructor(val presenter: MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView>) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    var persons: List<Person>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return ViewHolder(view,presenter)
    }

    override fun getItemCount(): Int = persons?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        if (persons != null) holder.bind(persons?.get(position)) else Unit


    class ViewHolder(val view: View,val presenter: MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView>): RecyclerView.ViewHolder(view) {
        private val fullName: TextView = view.findViewById(R.id.fullName)
        private val delete: ImageView = view.findViewById(R.id.delete)
        private val edit: ImageView = view.findViewById(R.id.edit)
        private var pair: Pair<View,Person?>? = null

        init {
            delete.setOnClickListener {
                presenter.deletePerson(pair?.second!!)
                presenter.getView()?.deletePersonFromList(pair?.second!!)
            }
            view.setOnClickListener{

            }
            edit.setOnClickListener {
                presenter.getView()?.openUserDialog(pair?.second)
            }
        }

        fun bind(person: Person?) {
            pair = Pair(view, person)
            fullName.text = person?.name ?: ""
        }
    }
}