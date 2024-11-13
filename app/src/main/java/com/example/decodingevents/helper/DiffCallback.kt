package com.example.decodingevents.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.decodingevents.data.local.entity.Event

internal object DiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

}