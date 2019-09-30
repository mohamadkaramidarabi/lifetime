package com.example.lifetime.ui.base.view

import android.app.ProgressDialog
import com.example.lifetime.util.CommonUtil
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), MVPView {

    private var progressDialog: ProgressDialog? = null

    override fun showProgress() {
        hideProgress()
        progressDialog = CommonUtil.showLoadingDialog(this)

    }

    override fun hideProgress() {
        progressDialog?.let { if (it.isShowing)  it.cancel() }
    }
}