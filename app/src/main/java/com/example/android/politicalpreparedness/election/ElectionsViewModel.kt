package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch


enum class Status{
    LOADING,
    ERROR,
    DONE
}
enum class Filter{
    FOLLOWED,
    ALL
}

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    /*
creating repo
*/
    private val database = ElectionDatabase.getInstance(application)
    private val repo = ElectionRepository(database)


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


    /**
     * followed election list
     */
    private var _followedElectionList = MutableLiveData<List<Election>>()
    val followedElectionList : LiveData<List<Election>>
    get() = _followedElectionList

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

         repo.refreshElections()
         _status.value = Status.DONE

     }  catch (e : Exception)
     {
         _status.value = Status.ERROR
         _electionsList.value = ArrayList()
         _followedElectionList.value =ArrayList()
     }
        }

    }

    /**
     * making an observer to update recycler view
     */

    private val electionListObserver = Observer<List<Election>>{
        _electionsList.value = it
    }

    private var electionListLiveData : LiveData<List<Election>>

    /**
     * making an observer to update followed list for recycler view
     */
    private val followedElectionListObserver = Observer<List<Election>> {
        _followedElectionList.value = it
    }

    private var followedElectionListLiveData : LiveData<List<Election>>


    /*
    Initializing the list with values
     */
    init {
        electionListLiveData = repo.getElectionBasedOnFilter(Filter.ALL)
        electionListLiveData.observeForever(electionListObserver)

        followedElectionListLiveData = repo.getElectionBasedOnFilter(Filter.FOLLOWED)
        followedElectionListLiveData.observeForever(followedElectionListObserver)
        getElectionResponse()
    }







    //TODO: Create val and functions to populate
// live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info


}