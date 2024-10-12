package com.example.decodingevents.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.decodingevents.data.resource.ListEventsItem
import com.example.decodingevents.databinding.CardViewItemBinding
import com.example.decodingevents.ui.detail_events.DetailEventActivity

class EventsAdapter : ListAdapter<ListEventsItem, EventsAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: CardViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventsTitle.text = event.name
            Glide.with(binding.imgItem.context).load(event.imageLogo).into(binding.imgItem)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailEventActivity::class.java)
            intent.putExtra("id_key", event.id.toString())
            holder.itemView.context.startActivity(intent)
        }
    }
}