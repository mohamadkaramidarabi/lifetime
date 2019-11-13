package com.example.lifetime.util

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.loadImage(context: Context, url: String, @DrawableRes placeholder: Int) {
    Glide
        .with(context)
        .load(url)
        .centerCrop()
        .placeholder(placeholder)
        .into(this)
}