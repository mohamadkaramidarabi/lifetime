package com.example.lifetime.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.lifetime.R
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.login.LoginActivity
import com.example.lifetime.ui.main.main_activity.MainActivity
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashInteractor.SplashMVPView {

    @Inject
    lateinit var presenter: SplashPresenter<SplashInteractor.SplashMVPView>

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        presenter.onAttach(this)
//        presenter.seedLifeExpectancies()
    }


    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
