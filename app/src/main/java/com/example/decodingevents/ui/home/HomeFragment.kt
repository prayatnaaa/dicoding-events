package com.example.decodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.data.remote.resource.ListEventsItem
import com.example.decodingevents.databinding.FragmentHomeBinding
import com.example.decodingevents.ui.events.EventsAdapter
import com.example.decodingevents.ui.events.EventsViewModel

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

        homeViewModel.isError.observe(viewLifecycleOwner) {
            error -> setError(error)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            loading -> setLoading(loading)
        }

    }

    private fun setLoading(loading: Boolean) {
        if (!loading) {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun setError(error: Boolean) {
        if (error) {
            Toast.makeText(requireActivity(), "${homeViewModel.errorMessage}", Toast.LENGTH_SHORT).show()
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