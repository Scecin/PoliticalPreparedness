package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    // ViewModel
    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val application = requireActivity().application
        val arg = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = arg.argElectionId
        val division = arg.argDivision


        // viewModel
        val viewModelFactory = VoterInfoViewModelFactory(electionId, division, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        // Binding values
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Loading of URLs
        viewModel.votingLocationsUrl.observe(viewLifecycleOwner, {
            it?.let {
                setIntent(it)
                viewModel.onVotingLocationNavigated()
            }
        })

        viewModel.ballotInformationUrl.observe(viewLifecycleOwner, {
            it?.let {
                setIntent(it)
                viewModel.onBallotInformationNavigated()
            }
        })

        // Save button UI state
        viewModel.isElectionFollowed.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.followElectionButton.visibility = View.GONE
                    binding.unfollowElectionButton.visibility = View.VISIBLE
                    Toast.makeText(context, R.string.follow_button, Toast.LENGTH_LONG).show()
                } else {
                    binding.followElectionButton.visibility = View.VISIBLE
                    binding.unfollowElectionButton.visibility = View.GONE
                    Toast.makeText(context, R.string.unfollow_button, Toast.LENGTH_LONG).show()
                }
            }
        })

        return binding.root
    }

    // Method to load URL intents
    private fun setIntent(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}