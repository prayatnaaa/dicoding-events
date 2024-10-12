package com.example.decodingevents.ui.finishedEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.data.resource.ListEventsItem
import com.example.decodingevents.databinding.FragmentFinishedEventsBinding
import com.example.decodingevents.ui.EventsAdapter
import com.example.decodingevents.ui.EventsViewModel


class FinishedEventsFragment : Fragment() {

    private var _binding: FragmentFinishedEventsBinding? = null
    private val binding get() = _binding!!
    private val finishedEventsViewModel by viewModels<EventsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinishedEvents.layoutManager = layoutManager

        finishedEventsViewModel.listEvents("0")

        finishedEventsViewModel.listEvents.observe(viewLifecycleOwner) {
            listFinishedEvents -> setListEvent(listFinishedEvents)
        }

    }

    private fun setListEvent(listFinishedEvents: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(listFinishedEvents)
        binding.rvFinishedEvents.adapter = adapter
    }
}