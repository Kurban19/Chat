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
import kotlinx.android.synthetic.main.item_text_message.*
import com.shkiper.chat.R
import com.shkiper.chat.extensions.shortFormat
import com.shkiper.chat.models.BaseMessage
import com.shkiper.chat.models.TextMessage

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessagesItemViewHolder>()  {

    private var items: List<TextMessage> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MessagesItemViewHolder(inflater.inflate(R.layout.item_text_message, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MessagesItemViewHolder, position: Int) {
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


   inner class MessagesItemViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
           LayoutContainer {


        override val containerView: View?
            get() = itemView


       fun bind(item: BaseMessage, holder: MessagesItemViewHolder){

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
}
