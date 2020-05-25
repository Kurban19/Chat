package ru.skillbranch.chat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_text_message.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.extensions.format
import ru.skillbranch.chat.extensions.shortFormat
import ru.skillbranch.chat.models.BaseMessage
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.Chat
import java.util.zip.Inflater

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessagesItemViewHolder>()  {

    var items: List<BaseMessage> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MessagesItemViewHolder(inflater.inflate(R.layout.item_text_message, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MessagesItemViewHolder, position: Int) {
        holder.bind(items[position])
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


   inner class MessagesItemViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
           LayoutContainer {


        override val containerView: View?
            get() = itemView


       fun bind(item: BaseMessage){
           if(item is TextMessage){
               tv_message_text.text = item.text
           }

           tv_message_time.text = item.date.shortFormat()
       }
   }

}
