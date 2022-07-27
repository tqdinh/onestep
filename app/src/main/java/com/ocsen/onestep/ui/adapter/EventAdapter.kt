package com.ocsen.onestep.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ocsen.onestep.data.entities.ItemDetail
import com.ocsen.onestep.databinding.CustomItemBinding

class EventAdapter(

    private val onItemClickListener: OnItemClickListener
) : ListAdapter<ItemDetail, EventAdapter.EventViewHolder>(object :
    DiffUtil.ItemCallback<ItemDetail>() {
    override fun areItemsTheSame(oldItem: ItemDetail, newItem: ItemDetail): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ItemDetail, newItem: ItemDetail): Boolean {
        return oldItem.equals(newItem)

    }
}) {

    interface OnItemClickListener {
        fun onItemClick(item: ItemDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
         val isActive=true
        val binding = CustomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (isActive) {
            ActiveEventViewHolder(binding)
        } else {
            RecommendEventViewHolder(binding)
        }
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

        override fun bind(item: ItemDetail, position: Int) {
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

    inner class RecommendEventViewHolder(binding: CustomItemBinding) :
        EventViewHolder(binding, onItemClickListener) {
        init {
            val layoutParams = binding.layoutItemEvent.layoutParams as ViewGroup.MarginLayoutParams
//            layoutParams.bottomMargin = dp2px(10F)
//            binding.btnGetStarted.visibility = View.VISIBLE
        }

        override fun bind(item: ItemDetail, position: Int) {
            super.bind(item, position)
//            if (!item.rewards.isNullOrEmpty()) {
//                val sortedRewards = item.rewards.sortedByDescending { it.numPoint }
//                binding.rewardEvent.text = itemView.context.getString(
//                    R.string.title_win_up_to,
//                    convertBobToCompactNotationString(sortedRewards.first().numPoint)
//                )
//            }
//            downloadImage(item, mapPhotoDetail) { getImageUrlFromId(it, position) }
//            if (!item.status.isNullOrEmpty()) {
//                if (item.status.equals(CommonConstant.EventStatus.DRAFT.type))
//                    binding.tvRolloutToAdmins.visibility = View.VISIBLE
//                else
//                    binding.tvRolloutToAdmins.visibility = View.GONE
//            }
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

        open fun bind(item: ItemDetail, position: Int) {
//            binding.txtTitleEvent.text = Util.parseHtmlToStyledTextOrEmpty(item.title)
//            binding.descEvent.text = Util.parseHtmlToStyledTextOrEmpty(item.shortDesc)
//            binding.btnContinue.setOnClickListener { onItemClickListener.onItemClick(item) }
            binding.btnGetStarted.setOnClickListener { onItemClickListener.onItemClick(item) }
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