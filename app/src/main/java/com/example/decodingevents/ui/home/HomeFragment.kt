package com.example.decodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.data.resource.ListEventsItem
import com.example.decodingevents.databinding.FragmentHomeBinding
import com.example.decodingevents.ui.EventsAdapter
import com.example.decodingevents.ui.EventsViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<EventsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val horizontalLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val verticalLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.rvUpcomingHome.layoutManager = horizontalLayoutManager
        binding.rvFinishedHome.layoutManager = verticalLayoutManager

        homeViewModel.listEvents("1")
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) {
            upcomingEventList(it)
        }

        homeViewModel.listEvents("0")
        homeViewModel.finishedEvents.observe(viewLifecycleOwner) {
            finishedEventList(it)
        }

    }

    private fun finishedEventList(list: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(list.take(5))
        binding.rvFinishedHome.adapter = adapter
    }

    private fun upcomingEventList(list: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(list.take(5))
        binding.rvUpcomingHome.adapter = adapter
    }
}