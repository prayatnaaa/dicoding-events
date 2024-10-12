package com.example.decodingevents.ui.finished_events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        finishedEventsViewModel.finishedEvents.observe(viewLifecycleOwner) { listFinishedEvents ->
            setListEvent(listFinishedEvents)
        }

        finishedEventsViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            setLoading(loading)
        }

        finishedEventsViewModel.isError.observe(viewLifecycleOwner) { error ->
            setError(error)
        }

    }

    private fun setError(error: Boolean) {
        if (error) {
            Toast.makeText(
                requireActivity(),
                "${finishedEventsViewModel.errorMessage}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun setLoading(loading: Boolean) {
        if (!loading) {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setListEvent(listFinishedEvents: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(listFinishedEvents)
        binding.rvFinishedEvents.adapter = adapter
    }
}