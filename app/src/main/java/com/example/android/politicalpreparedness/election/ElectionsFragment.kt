package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    private val _viewModel : ElectionsViewModel by viewModels()

    //TODO: Declare ViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding = FragmentElectionBinding.inflate(inflater)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener{
                _viewModel.displayVoterInfoDetails(it)
        })

        binding.followedElectionRecylerView.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener{
            _viewModel.displayVoterInfoDetails(it)
        })

        _viewModel.electionList.observe(viewLifecycleOwner, {
            binding.recyclerView.adapter
        })


        _viewModel.navigateToVoterInfoFragment.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id,it.division))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                _viewModel.doneNavigatingToVoterInfoFragment()
            }

        })

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}