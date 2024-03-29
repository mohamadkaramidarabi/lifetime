package com.example.lifetime.ui.main.main_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
class PersonAdapter constructor(val personEvent: (person: Person, action: PersonAction) -> (Unit)) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    private var checkPersonId: Int = 0

    var persons: List<Person>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = persons?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        if (persons != null) holder.bind(persons?.get(position)) else Unit


    fun setCheckLastPerson(person: Person) {
        checkPersonId = person.id
        notifyDataSetChanged()
    }


    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val fullName: TextView = view.findViewById(R.id.fullName)
        private val delete: ImageView = view.findViewById(R.id.delete)
        private val edit: ImageView = view.findViewById(R.id.edit)
        private val check = view.findViewById<ImageView>(R.id.ivCheck)
        private var pair: Pair<View,Person?>? = null

        init {
            delete.setOnClickListener {
                personEvent(pair?.second!!,PersonAction.DELETE)
            }
            view.setOnClickListener{
                val p = pair?.second!!.copy()
                p.id = pair?.second!!.id
                p.email = pair?.second!!.email
                p.isMainUser = pair?.second!!.isMainUser
                checkPersonId = p.id
                personEvent(p,PersonAction.VIEW)
            }
            edit.setOnClickListener {
                val p = pair?.second!!.copy()
                p.id = pair?.second!!.id
                p.email = pair?.second!!.email
                p.isMainUser = pair?.second!!.isMainUser
                notifyDataSetChanged()
                personEvent.invoke( p,PersonAction.EDIT)
            }
        }

        fun bind(person: Person?) {
            if (person?.isMainUser!!) {
                delete.visibility = View.GONE
            }
            if (person.id == checkPersonId) {
                check.visibility = View.VISIBLE
            }else {
                check.visibility = View.INVISIBLE
            }
            pair = Pair(view, person)
            fullName.text = person.name
        }
    }

}
enum class PersonAction{
    DELETE,EDIT,VIEW
}