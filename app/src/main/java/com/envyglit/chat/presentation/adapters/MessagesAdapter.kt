package com.envyglit.chat.presentation.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.envyglit.chat.R
import com.envyglit.chat.databinding.ItemGroupMessageBinding
import com.envyglit.chat.databinding.ItemMessageBinding
import com.envyglit.chat.domain.entities.BaseMessage
import com.envyglit.chat.domain.entities.ImageMessage
import com.envyglit.chat.domain.entities.TextMessage
import com.envyglit.chat.util.extensions.shortFormat
//import com.envyglit.chat.presentation.glide.GlideApp
import com.envyglit.chat.util.StorageUtils
import com.envyglit.chat.util.extensions.gone
import com.envyglit.chat.util.extensions.visible

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.AbstractViewHolder>()  {

    companion object{
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when(items[position].group){
        false -> SINGLE_TYPE
        true -> GROUP_TYPE
    }

    private var items: List<BaseMessage> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return when (viewType) {
            SINGLE_TYPE -> MessagesViewHolder(
                ItemMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> GroupMessagesViewHolder(
                ItemGroupMessageBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bind(items[position], holder)
    }

    fun updateData(data: List<BaseMessage>){

        val diffCallback = object : DiffUtil.Callback(){
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id == data[newPos].id

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].hashCode() == data[newPos].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    abstract inner class AbstractViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        abstract fun bind(item: BaseMessage, holder: AbstractViewHolder)

    }

    inner class MessagesViewHolder(private val binding: ItemMessageBinding) :
        AbstractViewHolder(binding) {

        override fun bind(item: BaseMessage, holder: AbstractViewHolder) {

            if (item.from.id == FirebaseAuth.getInstance().currentUser?.uid.orEmpty()) {
                binding.messageRoot.apply {
                    setBackgroundResource(R.drawable.rect_round_blue)
                    val lParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.END
                    )
                    this.layoutParams = lParams
                }
            } else {
                holder.itemView.findViewById<View>(R.id.message_root).apply {
                    setBackgroundResource(R.drawable.rect_round_primary_color)
                    val lParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.START
                    )
                    this!!.layoutParams = lParams
                }
           }

           if(item is TextMessage) {
               binding.tvMessageText.visible()
               binding.ivMessageImage.gone()
               binding.tvMessageText.text = item.text
           }
           else if(item is ImageMessage) {
               binding.tvMessageText.gone()
               binding.ivMessageImage.visible()
//               GlideApp.with(itemView)
//                   .load(StorageUtils.pathToReference(item.image))
//                   .into(binding.ivMessageImage)
           }

            binding.tvMessageTime.text = item.date.shortFormat()
       }
   }

    inner class GroupMessagesViewHolder(private val binding: ItemGroupMessageBinding) :
        AbstractViewHolder(binding) {

        override fun bind(item: BaseMessage, holder: AbstractViewHolder) {

            if (item.from.id == FirebaseAuth.getInstance().currentUser?.uid.orEmpty()) {
                binding.groupMessageRoot.apply {
                    setBackgroundResource(R.drawable.rect_round_blue)
                    val lParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.END
                    )
                    this.layoutParams = lParams
                    binding.tvGroupMessageAuthor.visibility = View.GONE
                }
            } else {
                binding.groupMessageRoot.apply {
                    setBackgroundResource(R.drawable.rect_round_primary_color)
                    val lParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.START
                    )
                    this.layoutParams = lParams
                    binding.tvGroupMessageAuthor.apply {
                        visibility = View.VISIBLE
                        text = item.from.firstName
                    }
                }
            }

            if(item is TextMessage) {
                binding.tvGroupMessageText.visible()
                binding.ivGroupMessageImage.gone()
                binding.tvGroupMessageText.text = item.text
            }
            else if(item is ImageMessage) {
                binding.tvGroupMessageText.gone()
                binding.ivGroupMessageImage.visible()
//                GlideApp.with(itemView)
//                    .load(StorageUtils.pathToReference(item.image))
//                    .into(binding.ivGroupMessageImage)
            }

            binding.tvGroupMessageTime.text = item.date.shortFormat()
        }
    }


}
