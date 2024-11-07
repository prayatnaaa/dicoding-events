package com.example.decodingevents.ui.events.finished_events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.R
import com.example.decodingevents.data.source.Result
import com.example.decodingevents.databinding.FragmentFinishedEventsBinding
import com.example.decodingevents.ui.events.EventViewModelFactory
import com.example.decodingevents.ui.events.EventsAdapter
import com.example.decodingevents.ui.events.EventsViewModel


class FinishedEventsFragment : Fragment() {

    private var _binding: FragmentFinishedEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventsBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.rvFinishedEvents.layoutManager = layoutManager

        eventViewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            getString(R.string.error_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val eventData = result.data
                        mAdapter.submitList(eventData)
                    }
                }
                binding.rvFinishedEvents.adapter = mAdapter
            }
        }

    }
}