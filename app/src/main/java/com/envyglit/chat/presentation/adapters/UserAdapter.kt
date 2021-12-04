package com.envyglit.chat.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.envyglit.chat.databinding.ItemUserListBinding
import com.envyglit.chat.util.StorageUtils
import com.envyglit.core.ui.entities.user.UserItem
import com.envyglit.core.ui.glide.GlideApp

class UserAdapter(private val listener: (UserItem) -> Unit): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var items: List<UserItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(items[position], listener)

    fun updateData(data: List<UserItem>) {

        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].id == data[newPos].id

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(private val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserItem, listener: (UserItem) -> Unit) {
            if (user.avatar != null) {
                GlideApp.with(itemView)
                    .load(StorageUtils.pathToReference(user.avatar!!))
                    .into(binding.ivAvatarUser)
            } else {
                GlideApp.with(itemView).clear(binding.ivAvatarUser)
                binding.ivAvatarUser.setInitials(user.initials)
            }
            binding.svIndicator.visibility = if (user.isOnline) View.VISIBLE else View.GONE
            binding.tvUserName.text = user.fullName
            binding.tvLastActivity.text = user.lastActivity
            binding.ivSelected.visibility = if (user.isSelected) View.VISIBLE else View.GONE
            itemView.setOnClickListener { listener.invoke(user) }
        }
    }

}


