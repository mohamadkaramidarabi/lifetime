package com.example.lifetime.ui.splash

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.lifetime.R
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.login.LoginActivity
import com.example.lifetime.ui.main.main_activity.MainActivity
import com.example.lifetime.util.*
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashInteractor.SplashMVPView {
    companion object {
        const val TAG = "SplashActivity"
    }


    @Inject
    lateinit var presenter: SplashPresenter<SplashInteractor.SplashMVPView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        if (::presenter.isInitialized) {
            presenter.onAttach(this)
        }
        setUp()
    }

    private fun setUp() {
        resetView()
    }

    private fun resetView() {
        btnSubmit.text = LocaleController.getString(R.string.next)
        tvChooseLang.text = LocaleController.getString(R.string.choose_your_language)
        Glide.with(this).load(AndroidUtilities.getImage(this,"spiral")).into(findViewById(R.id.ivLogo))

        rbEnglish.setColor(getColorInt(R.color.colorAccent),getColorInt(R.color.colorAccent))
        rbPersian.setColor(getColorInt(R.color.colorAccent),getColorInt(R.color.colorAccent))

        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_selected)
                ),
            intArrayOf(
                Color.GRAY,
                Color.WHITE
            )
        )
        tvChooseLang.setTextColor(Color.WHITE)
        tvEnglish.setTextColor(colorStateList)
        tvPersian.setTextColor(colorStateList)

        val currentLang = LocaleController.getInstance().getCurrentLang()
        AppLogger.e("$TAG, current language is ${currentLang.shortName}")
        when (currentLang.shortName) {
            LocaleController.FA -> {
                rbPersian.setChecked(checked = true, animated = false)
                tvPersian.isSelected = true
            }
            LocaleController.EN -> {
                rbEnglish.setChecked(checked = true, animated = false)
                tvEnglish.isSelected =true
            }
            else -> {
                rbPersian.setChecked(checked = true, animated = false)
                tvPersian.isSelected = true
            }
        }

        llPersian.setOnClickListener {
            if (rbPersian.isChecked) return@setOnClickListener

            rbPersian.setChecked(true, animated = true)
            rbEnglish.setChecked(false, animated = true)

            LocaleController.getInstance().applyLanguage(
                this,
                LocaleController.getInstance().languages[LocaleController.PERSIAN_INDEX]
            )

            tvPersian.isSelected = true
            tvEnglish.isSelected = false

            resetView()
        }

        llEnglish.setOnClickListener {
            if (rbEnglish.isChecked) return@setOnClickListener

            rbPersian.setChecked(false, animated = true)
            rbEnglish.setChecked(true, animated = true)

            LocaleController.getInstance().applyLanguage(
                this,
                LocaleController.getInstance().languages[LocaleController.ENGLISH_INDEX]
            )

            tvPersian.isSelected = false
            tvEnglish.isSelected = true

            resetView()
        }

        btnSubmit.setOnClickListener {
            presenter.seedLifeExpectancies()
        }
    }

    override fun onFragmentAttached() {}

    override fun onFragmentDetached(tag: String) {}

    override fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
//        finish()
    }


    override fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
//        finish()
    }


    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}
