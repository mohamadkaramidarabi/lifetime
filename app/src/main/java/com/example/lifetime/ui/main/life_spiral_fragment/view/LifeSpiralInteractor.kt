package com.example.lifetime.ui.main.life_spiral_fragment.view

import android.graphics.Point
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView
import com.example.lifetime.ui.main.main_activity.MainActivity

object LifeSpiralInteractor {

    interface LifeSpiralMVPView: MVPView {
        fun setPointList(pointList: List<Point>)
        fun getMainActivity(): MainActivity?
    }


    interface LifeSpiralMVPPresenter<V : LifeSpiralMVPView> : MVPPresenter<V> {
        fun calculateDrawPointList(w: Int, h: Int, person: Person)
    }

}