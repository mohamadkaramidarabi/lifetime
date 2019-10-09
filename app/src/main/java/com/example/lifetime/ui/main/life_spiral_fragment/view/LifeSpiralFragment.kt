package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseFragment
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import javax.inject.Inject

class LifeSpiralFragment : BaseFragment() {


    private var lifeSpiral: LifeSpiral? = null
    @Inject
    lateinit var compositeDisposable: CompositeDisposable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_life_spiral, container, false)

    override fun setUp() {
        showProgress()
        this.lifeSpiral = view?.findViewById(R.id.lifeSpiral)
        compositeDisposable.add(
            lifeSpiral!!.finishDraw.compose(
                SchedulerProvider().ioToMainObservableScheduler()
            ).doOnNext {
                Log.d("TAG",it.toString())
                if(it) hideProgress() else showProgress()
            }
            .subscribe()
        )
        lifeSpiral?.reDraw(Person("fake name", 80, PersianCalendar().time.time)) ?: Unit

    }

    fun setLifeSpiralVisibility(visibility: Int) {
        lifeSpiral?.visibility=visibility
    }

    fun updateUI(person: Person) {
        lifeSpiral?.reDraw(person)
    }


}
