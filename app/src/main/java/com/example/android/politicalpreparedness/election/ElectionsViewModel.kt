package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.launch


enum class Status{
    LOADING,
    ERROR,
    DONE
}
//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel: ViewModel() {

    //The internal mutablelive data that store the status of the response
    private var _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of Elections
    // with new values
    private var _electionsList = MutableLiveData<List<Election>>()

    // The external LiveData interface to the Election is immutable,
    // so only this class can modify
    val electionList: LiveData<List<Election>>
    get() = _electionsList


   private var _navigateToVoterInfoFragment = MutableLiveData<Election>()
    val navigateToVoterInfoFragment : LiveData<Election>
    get() = _navigateToVoterInfoFragment

    //Single event
    fun doneNavigatingToVoterInfoFragment(){
        _navigateToVoterInfoFragment.value = null
    }

    fun displayVoterInfoDetails(election: Election) {
        _navigateToVoterInfoFragment.value = election
    }

    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections
    /**
     * Gets elections information from the CivicApi Retrofit service and
     * updates the [Election] [List] and [Status] [LiveData].
     */
    private fun getElectionResponse(){
        viewModelScope.launch {
            _status.value = Status.LOADING
     try {

         val response: ElectionResponse =   CivicsApi.retrofitService.getElections()
         _electionsList.value =   response.elections
         _status.value = Status.DONE

         Log.i("myTag","Status Code : ${_status.value}")
     }  catch (e : Exception)
     {
         _status.value = Status.ERROR
         _electionsList.value = ArrayList()
     }
        }

    }

    /*
    Initializing the list with values
     */
    init {
        getElectionResponse()
    }
    //TODO: Create val and functions to populate
// live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info


}