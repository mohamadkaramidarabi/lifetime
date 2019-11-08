package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseFragment
import com.example.lifetime.ui.main.main_activity.MainActivity
import javax.inject.Inject


class LifeSpiralFragment : BaseFragment(), LifeSpiralInteractor.LifeSpiralMVPView{


    @Inject
    lateinit var presenter: LifeSpiralInteractor.LifeSpiralMVPPresenter<LifeSpiralInteractor.LifeSpiralMVPView>

    var person: Person? = null
    var lifeSpiralView: LifeSpiralView? = null
        private set

    var year: TextView? = null
        private set
    var month: TextView? = null
        private set
    var day: TextView? = null
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
        year = view?.findViewById(R.id.yearTextView)
        month = view?.findViewById(R.id.monthTextView)
        day = view?.findViewById(R.id.dayTextView)
        presenter.getLastPerson()
    }

    override fun getPerson(person: Person) {
        this.person = person
    }

    override fun onResume() {
        super.onResume()
        presenter.getLastPerson()
        (activity as MainActivity).onLifeSpiralFragmentResumed(this)
    }

    override fun onDetach() {
        presenter.onDetach()
        super.onDetach()
    }


}
