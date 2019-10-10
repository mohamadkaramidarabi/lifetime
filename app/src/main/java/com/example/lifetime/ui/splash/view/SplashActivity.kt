package com.example.lifetime.ui.splash.view

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.lifetime.R
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.main.main_activity.view.MainActivity
import com.example.lifetime.ui.splash.interactor.SplashMVPInteractor
import com.example.lifetime.ui.splash.presenter.SplashPresenter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashMVPView {

    @Inject
    lateinit var presenter: SplashPresenter<SplashMVPView, SplashMVPInteractor>

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash)
        presenter.onAttach(this)


    }


    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
