package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val dao = ElectionDatabase.getInstance(requireContext()).electionDao
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val division  = VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
        val viewModelFactory = VoterInfoViewModelFactory(electionId,division,dao)

        binding.viewModel = ViewModelProvider(
            this, viewModelFactory).get(VoterInfoViewModel::class.java)

        return binding.root

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
    }

    //TODO: Create method to load URL intents

}