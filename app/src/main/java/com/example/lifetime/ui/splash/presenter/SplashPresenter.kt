package com.example.lifetime.ui.splash.presenter

import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.ui.splash.interactor.SplashMVPInteractor
import com.example.lifetime.ui.splash.view.SplashMVPView
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SplashPresenter<V: SplashMVPView, I: SplashMVPInteractor>
@Inject constructor(interactor: I,
                    compositeDisposable: CompositeDisposable,
                    schedulerProvider: SchedulerProvider) :
    BasePresenter<V,I>(interactor,compositeDisposable,schedulerProvider), SplashMVPPresenter<V,I> {

    override fun onAttach(view: V?) {
        super.onAttach(view)
        seedInDataBase()
    }

    private fun seedInDataBase() = interactor?.let {
        compositeDisposable.add(
            it.seedLifeExpectancies()
                .doOnNext{
                }
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe {
                    getView()?.let {
                        it.openMainActivity()
                    }
                })
    }
}