package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import android.content.Intent
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R



class VoterInfoFragment : Fragment() {
    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var viewModel: VoterInfoViewModel
    private var followedFlag : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val dao = ElectionDatabase.getInstance(requireActivity().application).electionDao
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val division  = VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
        val viewModelFactory = VoterInfoViewModelFactory(electionId,division,dao)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel
        binding.stateLocations.text = getString(R.string.voting_location)
        binding.stateLocations.movementMethod = LinkMovementMethod.getInstance()

        binding.stateBallot.text = getString(R.string.ballot_information)
        binding.stateBallot.movementMethod = LinkMovementMethod.getInstance()


        viewModel.checkElectionIsFollowedByUser.observe(viewLifecycleOwner, Observer {
            if(it!=null)
            {
                followedFlag=true
                binding.followButton.text = getString(R.string.unfollowElection)
            }else {

                binding.followButton.text = getString(R.string.followElection)
            }
        })


        viewModel.votingLocations.observe(viewLifecycleOwner,{url->
            if(url!=null)
            {
                binding.stateLocations.text = getString(R.string.voting_location,url)
                try{
                    loadUrl(url)
                }catch (e : Exception)
                {
                    Log.d("myTag","Error loading url: $url")
                }
                viewModel.doneLoadingVotingLocation()
            }

        })



        viewModel.ballotInformation.observe(viewLifecycleOwner,{
            url->
            if(url!=null)
            {
                binding.stateBallot.text = getString(R.string.ballot_information,url)
              try{
                  loadUrl(url)
              }catch (e : Exception)
              {
                  Log.d("myTag","Error loading url: $url")
              }


                viewModel.doneLoadingBallotInformation()
            }

        })





        return binding.root


        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            binding.followButton.setOnClickListener {
                if(followedFlag)
                {
                    viewModel.unFollowElection()
                }
                else
                    viewModel.followElection()
            }
    }

    private fun loadUrl(url : String){
        val uri: Uri = Uri.parse(url) // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}