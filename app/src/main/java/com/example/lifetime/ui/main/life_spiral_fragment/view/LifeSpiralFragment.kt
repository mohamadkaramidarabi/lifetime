package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseFragment
import ir.hamsaa.persiandatepicker.util.PersianCalendar

class LifeSpiralFragment : BaseFragment() {


    private var lifeSpiral: LifeSpiral? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_life_spiral, container, false)
    }

    override fun setUp() {
        this.lifeSpiral = view?.findViewById(R.id.lifeSpiral)
        lifeSpiral?.reDraw(Person("fake name", 80, PersianCalendar().time.time)) ?: Unit
    }

    fun setLifeSpiralVisibility(visibility: Int) {
        lifeSpiral?.visibility=visibility
    }

    fun updateUI(person: Person) {
        lifeSpiral?.reDraw(person)
    }


}
