package com.example.decodingevents.ui.upcomingEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.data.resource.ListEventsItem
import com.example.decodingevents.databinding.FragmentUpcomingEventsBinding
import com.example.decodingevents.ui.EventsAdapter
import com.example.decodingevents.ui.EventsViewModel

class UpcomingEventsFragment : Fragment() {

    private var _binding: FragmentUpcomingEventsBinding? = null
    private val binding get() = _binding!!
    private val upcomingEventsViewModel by viewModels<EventsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcomingEvents.layoutManager = layoutManager

        upcomingEventsViewModel.listEvents("1")

        upcomingEventsViewModel.listEvents.observe(viewLifecycleOwner) { events ->
            setListEvent(events)
        }

        upcomingEventsViewModel.isLoading.observe(viewLifecycleOwner) { loaded ->
            setLoading(loaded)
        }

        upcomingEventsViewModel.isError.observe(viewLifecycleOwner) { error ->
            setError(error)
        }
    }

    private fun setError(error: Boolean) {
        if (error) {
            Toast.makeText(requireActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLoading(loaded: Boolean) {
        if (!loaded) {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setListEvent(events: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(events)
        binding.rvUpcomingEvents.adapter = adapter
    }
}