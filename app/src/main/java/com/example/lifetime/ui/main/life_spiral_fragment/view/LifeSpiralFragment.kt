package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseFragment
import com.example.lifetime.ui.main.main_activity.publishPerson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_life_spiral.*
import javax.inject.Inject

class LifeSpiralFragment : BaseFragment() {


    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_life_spiral, container, false)

    override fun setUp() {
        view?.setBackgroundColor(Color.TRANSPARENT)
//        showLoading()
//        this.lifeSpiral = view?.findViewById(R.id.lifeSpiral)
//        compositeDisposable.add(
//            lifeSpiral!!.finishDraw.compose(
//                SchedulerProvider().ioToMainObservableScheduler()
//            ).doOnNext {
//                if(it) hideLoading() else showLoading()
//            }
//            .subscribe()
//        )
        publishPerson.subscribe {
            updateUI(it)
        }
//        lifeSpiral?.reDraw(Person("fake name", 80f, PersianCalendar().time.time)) ?: Unit

    }

    fun updateUI(person: Person) {
        lifeSpiral?.reDraw(person)
    }

    override fun onResume() {
        super.onResume()

    }


}
