package com.ocsen.onestep.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.Utils.DateTimeUtils
import com.ocsen.onestep.databinding.CustomItemBinding

class EventAdapter(

    private val onItemClickListener: OnItemClickListener
) : ListAdapter<LocalPlace, EventAdapter.EventViewHolder>(object :
    DiffUtil.ItemCallback<LocalPlace>() {
    override fun areItemsTheSame(oldItem: LocalPlace, newItem: LocalPlace): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: LocalPlace, newItem: LocalPlace): Boolean {
        return oldItem.equals(newItem)

    }
}) {

    interface OnItemClickListener {
        fun onItemClick(item: LocalPlace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CustomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveEventViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == currentList.size - 1) {
            return 1
        } else if (position == 0) {
            return 0
        }
        return 2
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ActiveEventViewHolder(binding: CustomItemBinding) :
        EventViewHolder(binding, onItemClickListener) {
        init {
            val layoutParams = binding.layoutItemEvent.layoutParams as ViewGroup.MarginLayoutParams

//            if (currentList.size == 1) {
//                layoutParams.leftMargin = dp2px(26F)
//                layoutParams.rightMargin = dp2px(26F)
//            }
//            if (currentList.size > 1) {
//                layoutParams.width = (getScreenWidth(itemView.context) * 0.85).toInt()
//                layoutParams.rightMargin = dp2px(10F)
//            }
//            binding.btnContinue.visibility = View.VISIBLE
            //      binding.descEvent.visibility = View.GONE
        }

        override fun bind(item: LocalPlace, position: Int) {
            super.bind(item, position)
            val layoutParams = binding.layoutItemEvent.layoutParams as ViewGroup.MarginLayoutParams
//            if (position == 0) {
//                layoutParams.leftMargin = dp2px(26F)
//            } else if (position == currentList.size - 1) {
//                layoutParams.leftMargin = 0
//                layoutParams.rightMargin = dp2px(26F)
//            }
//
//            checkStatusDownload(item)
//            downloadImage(item, mapPhotoDetail) { getImageUrlFromId(it, position) }
        }


    }

    open class EventViewHolder(
        val binding: CustomItemBinding,
        private val onItemClickListener: EventAdapter.OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
//        private val circularProgressDrawable: CircularProgressDrawable =
//            CircularProgressDrawable(itemView.context).apply {
//                strokeWidth = 5f
//                centerRadius = 30f
//                start()
//            }

        init {
            binding.imvEvent.clipToOutline = true
            bindPhoto("", binding.imvEvent)
        }

        open fun bind(item: LocalPlace, position: Int) {
            binding.txtTitleEvent.text = item.title
            binding.tvDateTime.text = DateTimeUtils.getDateString(item.timestamp)
//            binding.descEvent.text = Util.parseHtmlToStyledTextOrEmpty(item.shortDesc)
//            binding.btnContinue.setOnClickListener { onItemClickListener.onItemClick(item) }
            binding.btnGetStarted.setOnClickListener { onItemClickListener.onItemClick(item) }

            binding.root.setOnClickListener{
                onItemClickListener.onItemClick(item)
            }
        }


        private fun bindPhoto(url: String, imageView: ImageView) {
//            Glide.with(itemView.context)
//                .load(url)
//                .placeholder(circularProgressDrawable)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .into(imageView)
        }
    }

}