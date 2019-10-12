package com.example.lifetime.ui.base.view

import android.app.ProgressDialog
import com.example.lifetime.util.CommonUtil
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), MVPView, BaseFragment.CallBack {

    private var progressDialog: ProgressDialog? = null

    override fun showLoading() {
        hideLoading()
        progressDialog = CommonUtil.showLoadingDialog(this)

    }

    override fun hideLoading() {
        progressDialog?.let { if (it.isShowing)  it.cancel() }
    }


}