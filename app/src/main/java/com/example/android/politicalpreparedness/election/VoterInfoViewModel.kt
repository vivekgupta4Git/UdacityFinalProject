package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.FollowedElection
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val electionId : Int,
    private val division: Division,
    private val dataSource: ElectionDao
    ) : ViewModel() {

   private var _voterInfoResponse : MutableLiveData<VoterInfoResponse>? = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse : LiveData<VoterInfoResponse>?
    get() = _voterInfoResponse

    private var _division = MutableLiveData<Division>()
    val div :  LiveData<Division>
    get() = _division

    private var _votingLocations = MutableLiveData<String>()
    val votingLocations : LiveData<String>
    get() = _votingLocations


    private var _ballotInformation = MutableLiveData<String>()
    val ballotInformation :LiveData<String>
    get() = _ballotInformation

    fun loadVotingLocation(){
        _votingLocations.value = _voterInfoResponse?.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun doneLoadingVotingLocation(){
        _votingLocations.value = null
    }


    fun loadBallotInformation(){
        _ballotInformation.value = _voterInfoResponse?.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    fun doneLoadingBallotInformation(){
        _ballotInformation.value = null
    }

    init {

            getResponseFromVoterInfo()
    }

    /*
    * https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyBMpuRIA_XCRwPrgHPLHMtjmbkPauib9hs&address=US/state:&electionId=2000
    * when the state in address field is empty , we get error response, so we need to put some value here ,
    * using api guide, putting ks =Kansas city as default so,
    * https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyBMpuRIA_XCRwPrgHPLHMtjmbkPauib9hs&address=US/state:ks&electionId=2000
     */

    private fun getResponseFromVoterInfo(){
        viewModelScope.launch {

        var address = division.country + "/" + division.state


            /**
             * I don't know why isEmptyOrNull() not working on state.
             */

            if(division.state.length <=0 )        {
            address =division.country + "/ks"
        }

            try{
                _voterInfoResponse?.value =    CivicsApi.retrofitService.getVoterInfo(address,electionId)
                Log.d("myTag","Response : ${voterInfoResponse?.value?.election?.id}")
            }catch (e : Exception)
            {
                Log.d("myTag","Error Occurred : ${e.message}")
            }


        }
    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs



    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in
     * multiple ways.
     * It is directly related to how elections
     * are saved/removed from the database.
     */


    /**
     * we need a live data that will be observed in the fragment which will tell the about the button text whether follow or unfollow
     * this live data gets its value from db
     * We loaded election details with given election id has not been followed by user than we will look out in the database first that particular
     * election id is present or not ,
     * if not present then button will have text unfollow
     * otherwise follow
     */

    val checkElectionIsFollowedByUser : LiveData<FollowedElection>
    get() = dataSource.isElectionFollowedOrNot(electionId)

    fun followElection(){
        viewModelScope.launch {
            dataSource.insertIntoFollowElection(electionId)
        }
    }

    fun unFollowElection(){
        viewModelScope.launch {
            dataSource.deleteFromFollow(electionId)
        }
    }

}