package ru.skillbranch.chat.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.skillbranch.chat.models.data.Chat

class MessagesAdapter(val listener: (Chat)->Unit) : RecyclerView.Adapter<MessagesAdapter.MessagesItemViewHolder>()  {


   inner class MessagesItemViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView),
           LayoutContainer {


        override val containerView: View?
            get() = itemView


       fun bind(){

       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MessagesItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}