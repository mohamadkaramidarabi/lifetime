package com.example.lifetime.ui.login

import android.os.Bundle
import com.example.lifetime.R
import com.example.lifetime.helper.AutoCompleteCountryAdapter
import com.example.lifetime.ui.base.view.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginInteractor.LoginMVPView{

    @Inject
    lateinit var presenter: LoginInteractor.LoginMVPPresenter<LoginInteractor.LoginMVPView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onAttach(this)
        presenter.loadAllCountries()

    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun openMainActivity() {
    }

    override fun onRegisterButtonClicked() {
    }

    override fun getAllCountries(countries: List<String>) {
        etCountry.setAdapter(AutoCompleteCountryAdapter(this,R.layout.row_country,
            ArrayList(countries)
        ))

    }

}
