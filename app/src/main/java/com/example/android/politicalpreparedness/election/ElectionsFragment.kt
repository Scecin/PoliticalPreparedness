package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Binding values
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val viewModelFactory = ElectionsViewModelFactory(requireActivity().application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)

        binding.viewModel = viewModel
        /**
         * Upcoming Elections
         */
        val upcomingElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
            election.let {
                viewModel.onElectionClicked(election)
            }
        })

        binding.upcomingList.adapter = upcomingElectionsAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                upcomingElectionsAdapter.submitList(it)
            }
        })

        /**
         * Saved Elections
         */
        val savedElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionClicked(election)
        })

        binding.savedList.adapter = savedElectionsAdapter

        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            it?.let {
                savedElectionsAdapter.submitList(it)
            }
        })

        /**
         * Navigation
         */
        viewModel.navigateToElection.observe(viewLifecycleOwner, Observer { election ->
            election?.let {
                this.findNavController().navigate(
                        ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                                it.id, it.division))
                viewModel.onElectionNavigated()
            }

        })

        return binding.root
    }

}