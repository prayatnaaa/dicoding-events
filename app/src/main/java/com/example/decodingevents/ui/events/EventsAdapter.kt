package com.example.decodingevents.ui.events

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.decodingevents.data.local.entity.Event
import com.example.decodingevents.databinding.CardViewItemBinding
import com.example.decodingevents.ui.detail_events.DetailEventActivity

class EventsAdapter : ListAdapter<Event, EventsAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: CardViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.tvEventsTitle.text = event.name
            Glide.with(binding.imgItem.context).load(event.imageLogo).into(binding.imgItem)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CardViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailEventActivity::class.java)
            intent.putExtra(DetailEventActivity.ID_KEY, event.id.toString())
            intent.putExtra(DetailEventActivity.ID_ACTIVE, event.isActive)
            holder.itemView.context.startActivity(intent)
        }
    }
}