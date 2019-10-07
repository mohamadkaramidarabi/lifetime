package com.example.lifetime.ui.lifespiralview


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifetime.R

class LifeSpiralFragment : Fragment() {
    private lateinit var lifeSpiral: LifeSpiral

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_life_spiral, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.lifeSpiral = view.findViewById(R.id.lifeSpiral)
    }

    fun setLifeSpiralVisibility(visibility: Int) {
        lifeSpiral.visibility=visibility
    }


}
