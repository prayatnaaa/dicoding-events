package com.example.decodingevents.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decodingevents.data.source.Result
import com.example.decodingevents.databinding.FragmentHomeBinding
import com.example.decodingevents.ui.events.EventViewModelFactory
import com.example.decodingevents.ui.events.EventsAdapter
import com.example.decodingevents.ui.events.EventsViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
            GridLayoutManager(requireActivity(), 2)

        binding.rvUpcomingHome.layoutManager = horizontalLayoutManager
        binding.rvFinishedHome.layoutManager = verticalLayoutManager

        val viewModelProvider = EventViewModelFactory.getInstance(requireActivity())
        val homeViewModel: EventsViewModel by viewModels {
            viewModelProvider
        }

        val activeAdapter = EventsAdapter()
        val finishedAdapter = EventsAdapter()

        binding.rvUpcomingHome.adapter = activeAdapter
        binding.rvFinishedHome.adapter = finishedAdapter


        homeViewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            Log.d("HomeFragment", result.toString())
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val eventData = result.data.take(5)
                            Log.d("HomeFragment", "Finish: $eventData")
                            finishedAdapter.submitList(eventData)
                        }
                    }
                }
            }
        homeViewModel.getActiveEvents().observe(viewLifecycleOwner) { result ->
            Log.d("HomeFragment", "upcoming$result")
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val eventData = result.data.take(5)
                        Log.d("HomeFragment", eventData.toString())
                        activeAdapter.submitList(eventData)
                    }
                }
            }
        }
    }

}