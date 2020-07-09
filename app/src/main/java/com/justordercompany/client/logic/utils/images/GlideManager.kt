package com.justordercompany.client.logic.utils.images

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.justordercompany.client.base.AppClass

class GlideManager
{
    companion object
    {
        fun loadImageSimple(uri: String, img: ImageView)
        {
            Glide.with(AppClass.app)
                    .load(uri)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade(250))
                    .into(img)
        }

        fun loadImageSimpleCircle(uri: String, img: ImageView)
        {
            Glide.with(AppClass.app)
                    .load(uri)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .dontAnimate()
                    .into(img)
        }
    }
}