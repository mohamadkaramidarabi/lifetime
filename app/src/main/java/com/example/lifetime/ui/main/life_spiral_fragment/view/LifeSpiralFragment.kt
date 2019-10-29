package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
    private var lifeSpiral: LifeSpiral? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_life_spiral, container, false)


    override fun setUp() {
        presenter.onAttach(this)
        view?.setBackgroundColor(Color.TRANSPARENT)
        lifeSpiral = view?.findViewById(R.id.lifeSpiral)

        presenter.getLastPerson()

        notifayHideLifeSpiralView.doOnNext {
            if (it) {
                lifeSpiral?.visibility = View.VISIBLE
                lifeSpiral?.reDraw()
            } else {
                lifeSpiral?.visibility = View.INVISIBLE
            }
        }.subscribe()
    }

    override fun getPerson(person: Person) {
        this.person = person
        lifeSpiral?.setPerson(person)
        lifeSpiral?.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        Log.d("Tag","OnPause")
    }


    override fun onDetach() {
        presenter.onDetach()
        super.onDetach()
    }


}
