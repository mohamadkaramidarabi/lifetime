package com.example.lifetime.ui.main.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.lifetime.R
import com.example.lifetime.ui.main.SubmitDialog
import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.presenter.MainMVPPresenter
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainMVPView {

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
        SubmitDialog(this).show()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }


}
