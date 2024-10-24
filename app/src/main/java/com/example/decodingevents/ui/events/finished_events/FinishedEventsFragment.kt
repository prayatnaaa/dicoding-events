package com.example.decodingevents.ui.events.finished_events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.data.remote.resource.ListEventsItem
import com.example.decodingevents.databinding.FragmentFinishedEventsBinding
import com.example.decodingevents.ui.events.EventsAdapter
import com.example.decodingevents.ui.events.EventsViewModel


class FinishedEventsFragment : Fragment() {

    private var _binding: FragmentFinishedEventsBinding? = null
    private val binding get() = _binding!!
    private val finishedEventsViewModel by viewModels<EventsViewModel>()
    private val mList = ArrayList<ListEventsItem>()

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
            mList.clear()
            setListEvent(listFinishedEvents)
        }

        finishedEventsViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            setLoading(loading)
        }

        finishedEventsViewModel.isError.observe(viewLifecycleOwner) { error ->
            setError(error)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val adapter = EventsAdapter()
                val query = searchView.text
                searchBar.setText(query)
                if (query.isNotEmpty()) {
                    finishedEventsViewModel.searchEvent(query.toString(), adapter )
                }

                searchBar.onCancelPendingInputEvents()
                false
            }
        }
    }


    private fun setError(error: Boolean) {
        if (error) {
            finishedEventsViewModel.errorMessage.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { errorMessage ->
                    Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
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