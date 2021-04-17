package com.shkiper.chat.ui.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.extensions.LayoutContainer
import com.shkiper.chat.R
import com.shkiper.chat.extensions.shortFormat
import com.shkiper.chat.models.BaseMessage
import com.shkiper.chat.models.TextMessage
import kotlinx.android.synthetic.main.item_group_message.*
import kotlinx.android.synthetic.main.item_message.*

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.AbstractViewHolder>()  {

    companion object{
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when(items[position].group){
        false -> SINGLE_TYPE
        true -> GROUP_TYPE
    }


    private var items: List<TextMessage> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            SINGLE_TYPE -> MessagesViewHolder(inflater.inflate(R.layout.item_message, parent, false))
            else -> GroupMessagesViewHolder(inflater.inflate(R.layout.item_group_message, parent, false))
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bind(items[position], holder)
    }

    fun updateData(data: List<TextMessage>){

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

    abstract inner class AbstractViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView), LayoutContainer{
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: BaseMessage, holder: AbstractViewHolder)

    }

   inner class MessagesViewHolder(convertView: View) : AbstractViewHolder(convertView){

        override val containerView: View
            get() = itemView


       override fun bind(item: BaseMessage, holder: AbstractViewHolder) {

           if(item.from.id == FirebaseAuth.getInstance().currentUser!!.uid){
               holder.message_root.apply {
                   setBackgroundResource(R.drawable.rect_round_blue)
                   val lParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.END)
                   this!!.layoutParams = lParams
               }
           }
           else {
               holder.message_root.apply {
                   setBackgroundResource(R.drawable.rect_round_primary_color)
                   val lParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START)
                   this!!.layoutParams = lParams
               }
           }

           if(item is TextMessage){
               tv_message_text.text = item.text
           }

           tv_message_time.text = item.date.shortFormat()
       }
   }


    inner class GroupMessagesViewHolder(convertView: View) : AbstractViewHolder(convertView) {

        override val containerView: View
            get() = itemView


        override fun bind(item: BaseMessage, holder: AbstractViewHolder) {

            if(item.from.id == FirebaseAuth.getInstance().currentUser!!.uid){
                holder.group_message_root.apply {
                    setBackgroundResource(R.drawable.rect_round_blue)
                    val lParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.END)
                    this!!.layoutParams = lParams
                    tv_group_message_author.visibility = View.GONE
                }
            }
            else {
                holder.group_message_root.apply {
                    setBackgroundResource(R.drawable.rect_round_primary_color)
                    val lParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.START)
                    this!!.layoutParams = lParams
                    tv_group_message_author.apply {
                        visibility = View.VISIBLE
                        text = item.from.firstName
                    }
                }
            }

            if(item is TextMessage){
                tv_group_message_text.text = item.text
            }

            tv_group_message_time.text = item.date.shortFormat()
        }
    }


}
