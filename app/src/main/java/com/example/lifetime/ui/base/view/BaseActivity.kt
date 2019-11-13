package com.example.lifetime.ui.base.view

import android.app.ProgressDialog
import android.os.Bundle
import com.example.lifetime.util.CommonUtil
import com.example.lifetime.util.LocaleController
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), MVPView, BaseFragment.CallBack {

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleController.getInstance().applyLanguage(this,LocaleController.getInstance().getCurrentLang())
    }

    override fun showLoading() {
        hideLoading()
        progressDialog = CommonUtil.showLoadingDialog(this)

    }

    override fun hideLoading() {
        progressDialog?.let { if (it.isShowing)  it.cancel() }
    }


}