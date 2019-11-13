package com.example.lifetime.ui.about

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.example.lifetime.R
import com.example.lifetime.ui.base.view.BaseFragment
import com.example.lifetime.util.AndroidUtilities
import kotlinx.android.synthetic.main.activity_splash.*


class AboutFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun setUp() {
        Glide.with(this).load(AndroidUtilities.getImage(this.getBaseActivity() as Activity,"spiral")).into(ivLogo)
    }


}
