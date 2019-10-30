package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseFragment
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

val notifayHideLifeSpiralView: PublishSubject<Boolean> = PublishSubject.create()

class LifeSpiralFragment : BaseFragment(), LifeSpiralInteractor.LifeSpiralMVPView{


    @Inject
    lateinit var presenter: LifeSpiralInteractor.LifeSpiralMVPPresenter<LifeSpiralInteractor.LifeSpiralMVPView>

    var person: Person? = null
    var lifeSpiralView: LifeSpiralView? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_life_spiral, container, false)


    override fun setUp() {
        presenter.onAttach(this)
        view?.setBackgroundColor(Color.TRANSPARENT)
        lifeSpiralView = view?.findViewById(R.id.lifeSpiral)

        presenter.getLastPerson()

        notifayHideLifeSpiralView.doOnNext {
            if (it) {
                lifeSpiralView?.visibility = View.VISIBLE
                lifeSpiralView?.reDraw()
            } else {
                lifeSpiralView?.visibility = View.INVISIBLE
            }
        }.subscribe()
    }

    override fun getPerson(person: Person) {
        this.person = person
        lifeSpiralView?.setPerson(person)
    }




    override fun onDetach() {
        presenter.onDetach()
        super.onDetach()
    }


}
