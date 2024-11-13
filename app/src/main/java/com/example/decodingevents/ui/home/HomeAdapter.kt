package com.example.decodingevents.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.decodingevents.data.local.entity.Event
import com.example.decodingevents.databinding.HomeCardViewBinding
import com.example.decodingevents.helper.DiffCallback
import com.example.decodingevents.ui.detail_events.DetailEventActivity

class HomeAdapter : ListAdapter<Event, HomeAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: HomeCardViewBinding) : RecyclerView.ViewHolder((binding.root)) {
        fun bind(event: Event) {
            Glide.with(binding.imgItem.context).load(event.imageLogo).into(binding.imgItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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