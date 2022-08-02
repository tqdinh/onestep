package com.ocsen.onestep.ui.adapter

import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.data.local.entities.LocalImage
import com.ocsen.onestep.Utils.DateTimeUtils
import com.ocsen.onestep.databinding.LocalImageBinding

open class LocalImageViewHolder(
    val binding: LocalImageBinding,
    private val onItemClickListener: LocalImageAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    private val circularProgressDrawable: CircularProgressDrawable =
        CircularProgressDrawable(itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

    init {
        binding.imvEvent.clipToOutline = true
        bindPhoto("", binding.imvEvent)
    }

    open fun bind(item: LocalImage, position: Int) {
        bindPhoto(item.path, binding.imvEvent)

        binding.root.setOnClickListener({
            onItemClickListener.onItemClick(item)
        })
        binding.tvLatLng.text = "${item?.lat} -${item?.lon}"
        binding.timeStamp.text= DateTimeUtils.getDateString(item.timestamp)
        Log.d("IMAGE_TIMESTAMP",DateTimeUtils.getDateString(item.timestamp))
//        binding.txtTitleEvent.text = Util.parseHtmlToStyledTextOrEmpty(item.title)
//        binding.descEvent.text = Util.parseHtmlToStyledTextOrEmpty(item.shortDesc)
//        binding.btnContinue.setOnClickListener { onItemClickListener.onItemClick(item) }
//        binding.btnGetStarted.setOnClickListener { onItemClickListener.onItemClick(item) }
    }

    private fun bindPhoto(url: String, imageView: ImageView) {
        Glide.with(itemView.context)
            .load(url)
            .placeholder(circularProgressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageView)
    }
}
