package com.example.lifetime.ui.main.life_spiral_fragment.view

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView

object LifeSpiralInteractor {

    interface LifeSpiralMVPView: MVPView {
        fun getMainPerson(person: Person)
    }


    interface LifeSpiralMVPPresenter<V : LifeSpiralMVPView> : MVPPresenter<V> {
        fun getMainPersonFromDataBase(): Boolean
    }

}