package com.shkiper.chat.presentation.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.shkiper.chat.databinding.*
import com.shkiper.chat.presentation.glide.GlideApp
import com.shkiper.chat.domain.entities.data.ChatItem
import com.shkiper.chat.domain.entities.data.ChatType
import com.shkiper.chat.util.StorageUtils

class ChatAdapter(private val listener: (ChatItem)->Unit) : RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {
    companion object{
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    var items: List<ChatItem> = listOf()

    override fun getItemViewType(position: Int): Int = when(items[position].chatType){
        ChatType.ARCHIVE -> ARCHIVE_TYPE
        ChatType.SINGLE -> SINGLE_TYPE
        ChatType.GROUP -> GROUP_TYPE

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return when (viewType) {
            SINGLE_TYPE -> SingleViewHolder(
                ItemChatSingleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            GROUP_TYPE -> GroupViewHolder(
                ItemChatGroupBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> ArchiveViewHolder(
                ItemChatArchiveBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChatAdapter.ChatItemViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    fun updateData(data: List<ChatItem>){

        val diffCallback = object :DiffUtil.Callback(){
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id == data[newPos].id

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].hashCode() == data[newPos].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    abstract inner class ChatItemViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        abstract fun bind(item: ChatItem, listener: (ChatItem) -> Unit)
    }

    inner class SingleViewHolder(private val binding: ItemChatSingleBinding) :
        ChatItemViewHolder(binding),
        ChatItemTouchHelperCallback.ItemTouchViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {
            if (item.avatar == null) {
                GlideApp.with(itemView)
                    .clear(binding.ivAvatarSingle)
                binding.ivAvatarSingle.setInitials(item.initials)
            } else {
                GlideApp.with(itemView)
                    .load(StorageUtils.pathToReference(item.avatar))
                    .into(binding.ivAvatarSingle)
            }

            binding.svIndicator.visibility = if (item.isOnline) View.VISIBLE else View.GONE

            with(binding.tvDateSingle) {
                if (item.lastMessageDate != null) {
                    visibility = View.VISIBLE
                    text = item.lastMessageDate
                } else {
                    visibility = View.GONE
                }

            }

            with(binding.tvCounterSingle) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            binding.tvMessageSingle.text = item.shortDescription
            binding.tvTitleSingle.text = item.title

            itemView.setOnClickListener {
                listener.invoke(item)
            }

        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class GroupViewHolder(private val binding: ItemChatGroupBinding) :
        ChatItemViewHolder(binding),
        ChatItemTouchHelperCallback.ItemTouchViewHolder {

        @SuppressLint("SetTextI18n")
        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            binding.ivAvatarGroup.setInitials(item.title[0].toString())

            with(binding.tvDateGroup) {
                if (item.lastMessageDate != null) {
                    visibility = View.VISIBLE
                    text = item.lastMessageDate
                } else {
                    visibility = View.GONE
                }

            }

            with(binding.tvCounterGroup) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }

            binding.tvTitleGroup.text = item.title
            binding.tvMessageGroup.text = item.shortDescription
            with(binding.tvMessageAuthor) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = "${item.author} :"
            }


            itemView.setOnClickListener {
                listener.invoke(item)

            }

        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }


    inner class ArchiveViewHolder(private val binding: ItemChatArchiveBinding) :
        ChatItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            with(binding.tvDateArchive) {
                if (item.lastMessageDate != null) {
                    visibility = View.VISIBLE
                    text = item.lastMessageDate
                } else {
                    visibility = View.GONE
                }

            }

            with(binding.tvCounterArchive) {
                visibility = if (item.messageCount > 0) View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            binding.tvTitleArchive.text = item.title
            binding.tvMessageArchive.text = item.shortDescription

            itemView.setOnClickListener {
                listener.invoke(item)
            }

            with(binding.tvMessageAuthorArchive) {
                visibility = if (item.lastMessageDate != null) View.VISIBLE else View.GONE
                text = "@${item.author}"
            }

        }
        
    }

}