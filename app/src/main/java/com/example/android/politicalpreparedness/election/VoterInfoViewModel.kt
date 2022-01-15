package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val electionId : Int,
    private val division: Division,
    private val dataSource: ElectionDao
    ) : ViewModel() {

   private var _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse : LiveData<VoterInfoResponse>
    get() = _voterInfoResponse

    private var _division = MutableLiveData<Division>()
    val div :  LiveData<Division>
    get() = _division

    init {
            getResponseFromVoterInfo()
    }

    /*
    * https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyBMpuRIA_XCRwPrgHPLHMtjmbkPauib9hs&address=US/state:&electionId=2000
    * when the state in address field is empty , we get error response, so we need to put some value here , using api guide, putting ks =Kansas city as default
    * so,
    * https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyBMpuRIA_XCRwPrgHPLHMtjmbkPauib9hs&address=US/state:ks&electionId=2000
     */

    private fun getResponseFromVoterInfo(){
        viewModelScope.launch {

        var address = division.country
        if(division.state.isEmpty())
        {
            address =division.country + "/state:ks"
        }

         _voterInfoResponse.value =    CivicsApi.retrofitService.getVoterInfo(address,electionId)

            Log.i("myTag","Response : ${voterInfoResponse.value}")
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

}