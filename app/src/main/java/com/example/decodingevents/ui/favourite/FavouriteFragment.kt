package com.example.decodingevents.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.R
import com.example.decodingevents.databinding.FragmentUpcomingEventsBinding
import com.example.decodingevents.ui.events.EventViewModelFactory
import com.example.decodingevents.ui.events.EventsAdapter
import com.example.decodingevents.ui.events.EventsViewModel

class FavouriteFragment : Fragment() {

    private var _binding: FragmentUpcomingEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val eventFactory: EventViewModelFactory =
            EventViewModelFactory.getInstance(requireActivity())
        val eventViewModel: EventsViewModel by viewModels {
            eventFactory
        }
        val mAdapter = EventsAdapter()
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcomingEvents.layoutManager = layoutManager
        binding.rvUpcomingEvents.adapter = mAdapter

        eventViewModel.getFavouriteEvent().observe(viewLifecycleOwner) { favEvent ->

            if (favEvent == null) {
                Toast.makeText(requireActivity(), getString(R.string.empty_fav), Toast.LENGTH_SHORT).show()
            }

            binding.progressBar.visibility = View.GONE
            mAdapter.submitList(favEvent)
        }
    }
}