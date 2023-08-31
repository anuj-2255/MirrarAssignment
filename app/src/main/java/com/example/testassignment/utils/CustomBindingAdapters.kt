package com.example.testassignment.utils

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.testassignment.R

class CustomBindingAdapters {
    companion object {

        @JvmStatic
        @BindingAdapter("imageResourceId")
        fun loadResourceImage(view: ImageView, imageId: Int) {
            Glide.with(view.context).load(imageId).into(view)
        }

        @JvmStatic
        @BindingAdapter("tintImage")
        fun setTintImage(view: ImageView, colorCode: String) {
            view.setColorFilter(Color.parseColor(colorCode))
        }

        @JvmStatic
        @BindingAdapter(value = ["imageUrl", "placeholder", "transformation"], requireAll = false)
        fun loadRemoteImage(
            view: ImageView,
            imageUrl: String?,
            placeholder: Drawable?,
            transformation: Boolean = false
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                if (transformation) {
                    if (placeholder != null)

                        Glide.with(view.context)
                            .load(imageUrl)
                            .placeholder(placeholder)
                            .fitCenter()
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                            .into(view)
                    else Glide.with(view.context)
                        .load(imageUrl)
                        .placeholder(placeholder)
                        .fitCenter()
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                        .into(view)

                } else {
                    if (placeholder != null)

                        Glide.with(view.context)
                            .load(imageUrl)
                            .placeholder(placeholder)
                            .into(view)
                    else Glide.with(view.context)
                        .load(imageUrl)
                        .placeholder(placeholder)
                        .into(view)

                }
            } else if (placeholder != null) Glide.with(view.context)
                .load(R.drawable.banner_placeholder)
                .into(view)    //Picasso.get().load().into(view)

        }

        @JvmStatic
        @BindingAdapter("adapter")
        fun RecyclerView.setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
            if (adapter != null) {
                this.adapter = adapter
            }
        }

        @JvmStatic
        @BindingAdapter("itemDecoration")
        fun RecyclerView.setDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
            if (itemDecoration != null) {
                this.removeItemDecoration(itemDecoration)
                this.addItemDecoration(itemDecoration)
            }
        }

        @BindingAdapter("strikethrough")
        @JvmStatic
        fun strikethrough(view: TextView, show: Boolean) {
            view.paintFlags = if (show) {
                view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }


    }
}