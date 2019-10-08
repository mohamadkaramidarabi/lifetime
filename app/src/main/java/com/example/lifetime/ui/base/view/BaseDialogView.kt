package com.example.lifetime.ui.base.view

import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.lifetime.util.CommonUtil
import dagger.android.support.DaggerDialogFragment

abstract class BaseDialogView: DaggerDialogFragment(), DialogMVPView{

    private var parentActivity: BaseActivity? = null
    private var progressDialog: ProgressDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.parentActivity = activity
            activity?.onFragmentAttached()
        }
    }


    override fun show(manager: FragmentManager, tag: String?){
        val transaction= manager.beginTransaction()
        val prevFragment = manager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }


    override fun hideProgress() {
        if (progressDialog != null && progressDialog?.isShowing!!) {
            progressDialog?.cancel()
        }
    }

    override fun showProgress() {
        hideProgress()
        progressDialog = CommonUtil.showLoadingDialog(this.context)
    }


    fun dismissDialog(tag: String) {
        dismiss()
        getBaseActivity()?.onFragmentDetached(tag)
    }

    private fun getBaseActivity(): BaseActivity? {
        return parentActivity
    }
}