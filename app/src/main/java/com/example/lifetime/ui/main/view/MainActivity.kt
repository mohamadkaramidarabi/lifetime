package com.example.lifetime.ui.main.view

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.lifetime.R
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.addperson.view.AddPersonDialog
import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.presenter.MainMVPPresenter
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVPView {

    private lateinit var button: AppCompatButton

    @Inject
    lateinit var presenter: MainMVPPresenter<MainMVPView, MainMVPInteractor>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onAttach(this)

        button = findViewById(R.id.button)
        button.setOnClickListener{
            presenter.onButtonClicked()
        }
    }


    override fun openUserDialog() {
        AddPersonDialog().show(supportFragmentManager,null)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }


}
