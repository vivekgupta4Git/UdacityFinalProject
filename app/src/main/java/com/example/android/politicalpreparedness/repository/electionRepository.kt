package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.Filter
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database : ElectionDatabase) {

    suspend fun refreshElections(){
        try{
            withContext(Dispatchers.IO){
                val response  = CivicsApi.retrofitService.getElections()

                for(election in response.elections)
                {
                    database.electionDao.insert(election)
                }
            }
        }catch (e: Exception)
        {
            Log.i("myTag","Error Occurred: ${e.message}")
        }
    }

    fun getElectionBasedOnFilter(filter:Filter) : LiveData<List<Election>>{
    return when(filter)
    {
        Filter.FOLLOWED->{
            database.electionDao.getFollowedSavedElectionsOnly()
        }
        Filter.ALL->{
            database.electionDao.getAllElectionSaved()
        }
    }
    }

}