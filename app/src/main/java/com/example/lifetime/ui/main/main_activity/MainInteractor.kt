package com.example.lifetime.ui.main.main_activity

import android.graphics.Bitmap
import android.graphics.Point
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView

object MainInteractor {

    interface MainMVPView : MVPView {
        fun openUserDialog(person: Person?=null)
        fun loadPersons(persons: MutableList<Person>)
        fun deletePersonFromList(person: Person)
        fun getPersonFromList(person: Person)
        fun getLastPerson(person: Person)
        fun updateViewAfterDeleteCurrentPerson(mainPerson: Person)
        fun setPointList(pointList: List<Point>)
        fun setBitmap(bitmap: Bitmap)
    }

    interface MainMVPPresenter<V : MainMVPView> : MVPPresenter<V> {
        fun onButtonClicked()
        fun getPersons(): Boolean
        fun deletePerson(person: Person): Boolean
        fun onPersonClicked(person: Person)
        fun setLastPersonOnDb(person: Person)
        fun getLastPersonFromDb()
    }
}